package ex.rr.adminpanel.data.services;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import ex.rr.adminpanel.data.exceptions.UserInputException;
import ex.rr.adminpanel.data.models.templates.page.Action;
import ex.rr.adminpanel.ui.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

/**
 * The {@code QueryResultSetService} service class for SQL execution and processing based on ResultSet element.
 *
 * @author rromanowicz
 */
@Slf4j
@Service
public class QueryResultSetService {

    /**
     * Adds query to be used for processing.
     *
     * @param query         Query to be used for processing.
     * @param actions       Actions to be added in data grid.
     * @param selectionMode Result grid selection mode.
     * @return Grid<HashMap < String, Object>>    Grid with SQL query results.
     */
    public Grid<HashMap<String, Object>> query(String query, List<Action> actions, Grid.SelectionMode selectionMode) {
        validateQuery(query);
        Grid<HashMap<String, Object>> grid;
        try (Connection connection = Objects.requireNonNull(Utils.getUserSession()).getDataSource().getConnection()) {
            try (ResultSet queryResults = connection.prepareStatement(query).executeQuery()) {
                grid = toGrid(queryResults, actions, selectionMode);
            } catch (SQLException e) {
                return fallbackGrid("Query execution error", e.getMessage());
            }
        } catch (SQLException e) {
            return fallbackGrid("Connection error", e.getMessage());
        }

        return grid;
    }

    /**
     * Adds query to be used for processing.
     *
     * @param property      Name of the property with query.
     * @param replacements  Map with values to substitute in query.
     * @param selectionMode Result grid selection mode.
     * @return Grid<HashMap < String, Object>>    Grid with SQL query results.
     */
    public Grid<HashMap<String, Object>> propertyQuery(String property, Map<String, String> replacements, Grid.SelectionMode selectionMode) {
        Grid<HashMap<String, Object>> grid;
        try (Connection connection = Objects.requireNonNull(Utils.getUserSession()).getDataSource().getConnection()) {
            String dbName = connection.getMetaData().getDatabaseProductName();
            String query = (String) VaadinSession.getCurrent().getAttribute(dbName + "." + property);
            if (replacements != null) {
                for (Map.Entry<String, String> entry : replacements.entrySet()) {
                    query = query.replace(entry.getKey(), entry.getValue());
                }
            }
            try (ResultSet queryResults = connection.prepareStatement(query).executeQuery()) {
                grid = toGrid(queryResults, null, selectionMode);
            } catch (SQLException e) {
                return fallbackGrid("Query execution error", e.getMessage());
            }
        } catch (SQLException e) {
            return fallbackGrid("Connection error", e.getMessage());
        }

        return grid;
    }

    /**
     * Returns database views available for current db user.
     *
     * @return Grid<HashMap < String, Object>>
     */
    public Grid<HashMap<String, Object>> getTables() {
        return propertyQuery("tblQuery", null, Grid.SelectionMode.SINGLE);
    }

    /**
     * Returns columns available in given table/view.
     *
     * @param schema Schema name
     * @param table  Table name
     * @return Grid<HashMap < String, Object>>
     */
    public Grid<HashMap<String, Object>> getTableColumns(String schema, String table) {
        return propertyQuery("colQuery", Map.of("{SCHEMA}", schema, "{TABLE}", table), Grid.SelectionMode.MULTI);
    }

    /**
     * Transforms {@link ResultSet} into {@link Grid} containing results.
     *
     * @param rs            ResultSet to be processed.
     * @param actions       Actions to ba added to grid.
     * @param selectionMode selection mode to be applied on result Grid.
     * @return Grid<HashMap < String, Object>>
     */
    private Grid<HashMap<String, Object>> toGrid(ResultSet rs, List<Action> actions, Grid.SelectionMode selectionMode) {
        Grid<HashMap<String, Object>> grid = new Grid<>();
        grid.setSelectionMode(selectionMode);

        List<HashMap<String, Object>> rows = new LinkedList<>();

        List<String> columns = new LinkedList<>();
        ResultSetMetaData resultSetMetaData;
        try {
            resultSetMetaData = rs.getMetaData();
            int colCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i < colCount + 1; i++) {
                columns.add(resultSetMetaData.getColumnLabel(i));
            }

            while (rs.next()) {
                HashMap<String, Object> row = new LinkedHashMap<>();
                for (String col : columns) {
                    int colIndex = columns.indexOf(col) + 1;
                    String object = rs.getObject(colIndex) == null ? "" : String.valueOf(rs.getObject(colIndex));
                    row.put(col, object);
                }
                rows.add(row);
            }

            HashMap<String, Object> s = rows.get(0);
            for (Map.Entry<String, Object> entry : s.entrySet()) {
                grid.addColumn(h -> h.get(entry.getKey())).setHeader(entry.getKey());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        if (actions != null && !actions.isEmpty()) {
            actions.forEach(action -> grid.addComponentColumn(row -> {
                Button editButton = new Button(action.getName());
                editButton.addClickListener(e -> executeAction(action, row));
                return editButton;
            }).setWidth("150px").setFlexGrow(0));
        }

        grid.setItems(rows);
        return grid;
    }

    /**
     * Binds action button to data row.
     *
     * @param action {@link Action} to be added
     * @param row    Affected data row
     */
    private void executeAction(Action action, Map<String, Object> row) {
        String url = prepareString(action, row);
        switch (action.getAction()) {
            case LOG -> log.info(url);
            case REST -> log.info("REST Call: [{} {}]", action.getType(), url);
            case TEAMS_MESSAGE -> log.info("TEAMS_MESSAGE Publishing: [{}]", url);
        }
        Utils.displayNotification(NotificationVariant.LUMO_SUCCESS, "Success.", url);
    }

    /**
     * Method for parsing action's url string.
     *
     * @param action {@link Action} object with substitution values
     * @param row    Affected data row
     * @return url String with replaced values
     */
    private String prepareString(Action action, Map<String, Object> row) {
        String str = action.getUrl();

        for (Map.Entry<String, String> entry : action.getData()) {
            str = str.replace(String.format("${%s}", entry.getKey()), row.get(entry.getValue()).toString());
        }
        return str;
    }

    /**
     * Query validation.
     *
     * @param query query to be validated
     * @throws UserInputException Invalid data input.
     */
    private void validateQuery(String query) {
        Set<String> blockList = Set.of("--");
        String s = query.toUpperCase();

        List<String> violations = blockList.stream().filter(s::contains).toList();
        if (!violations.isEmpty()) {
            throw new UserInputException("Unexpected comment in 'SELECT' query.");
        }
    }

    private Grid<HashMap<String, Object>> fallbackGrid(String message, String reason) {
        Grid<HashMap<String, Object>> grid = new Grid<>();
        Utils.displayNotification(NotificationVariant.LUMO_ERROR, message, reason);
        return grid;
    }

    public Boolean query(DataSource dataSource, String data) {
        return getRowCount.apply(dataSource, data) > 0;
    }

    BiFunction<DataSource, String, Integer> getRowCount = (dataSource, s) -> {
        String query = String.format("SELECT COUNT(1) CNT FROM (%s)", s);
        try (Connection connection = dataSource.getConnection()) {
            try (ResultSet queryResults = connection.prepareStatement(query).executeQuery()) {
                queryResults.next();
                return queryResults.getInt("CNT");
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    };


}

