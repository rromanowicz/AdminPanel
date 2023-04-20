package ex.rr.adminpanel.data.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import ex.rr.adminpanel.data.exceptions.UserInputException;
import ex.rr.adminpanel.data.models.templates.page.Action;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * The {@code QueryService} service class for SQL execution and processing.
 *
 * @author rromanowicz
 */
@Slf4j
@Service
public class QueryTupleService {

    @Autowired
    EntityManager entityManager;

    private List<Tuple> queryResults;
    private List<Action> actions;

    /**
     * Adds query to be used for processing.
     *
     * @param query query to be used for processing
     * @return QueryService
     */
    public QueryTupleService withQuery(String query) {
        validateQuery(query);
        this.queryResults = entityManager.createNativeQuery(query, Tuple.class).getResultList();
        this.actions = null;
        return this;
    }

    /**
     * Adds actions to be added in result Grid
     *
     * @param actions List of {@link Action}
     * @return QueryService
     */
    public QueryTupleService withActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    /**
     * Executes query provided with 'withQuery()' method and returns JSON string of the results.
     *
     * @return JSON String
     */
    public String toJson() {
        if (queryResults.isEmpty())
            return null;

        List<ObjectNode> json = new ArrayList<ObjectNode>();
        ObjectMapper mapper = new ObjectMapper();
        for (Tuple t : queryResults) {
            List<TupleElement<?>> cols = t.getElements();
            ObjectNode one = mapper.createObjectNode();
            for (TupleElement col : cols) {
                one.put(col.getAlias(), t.get(col.getAlias()).toString());
            }
            json.add(one);
        }
        return json.toString();
    }

    /**
     * Executes query provided with 'withQuery()' method and returns List of Maps containing results.
     */
    public List<Map<String, String>> toMap() {
        if (queryResults.isEmpty())
            return null;

        List<Map<String, String>> list = new ArrayList<>();
        for (Tuple t : queryResults) {
            List<TupleElement<?>> cols = t.getElements();
            Map<String, String> map = new HashMap<>();
            for (TupleElement col : cols) {
                map.put(col.getAlias(), t.get(col.getAlias()).toString());
            }
            list.add(map);
        }
        return list;
    }

    /**
     * Executes query provided with 'withQuery()' method and returns {@link Grid} containing results.
     *
     * @param selectionMode selection mode to be applied on result Grid
     * @return Grid of Tuples
     */
    public Grid<Tuple> toGrid(Grid.SelectionMode selectionMode) {
        Grid<Tuple> grid = new Grid<>(Tuple.class, false);
        grid.setSelectionMode(selectionMode);

        queryResults.get(0).getElements().forEach(col -> grid.addColumn(c -> c.get(col.getAlias())).setHeader(col.getAlias()));

        if (actions != null && !actions.isEmpty()) {
            actions.forEach(action -> grid.addComponentColumn(tuple -> {
                Button editButton = new Button(action.getName());
                editButton.addClickListener(e -> executeAction(action, tuple));
                return editButton;
            }).setWidth("150px").setFlexGrow(0));
        }

        grid.setItems(queryResults);
        return grid;
    }

    /**
     * Binds action button to data row.
     *
     * @param action {@link Action} to be added
     * @param tuple  Affected data row
     */
    private void executeAction(Action action, Tuple tuple) {
        String url = prepareString(action, tuple);
        switch (action.getAction()) {
            case LOG -> log.info(url);
            case REST -> log.info("REST Call: [{} {}]", action.getType(), url);
            case TEAMS_MESSAGE -> log.info("TEAMS_MESSAGE Publishing: [{}]", url);
        }
    }

    /**
     * Method for parsing action's url string.
     *
     * @param action {@link Action} object with substitution values
     * @param tuple  Affected data row
     * @return url String with replaced values
     */
    private String prepareString(Action action, Tuple tuple) {
        String str = action.getUrl();

        for (Map.Entry<String, String> entry : action.getData()) {
            str = str.replace(String.format("${%s}", entry.getKey()), tuple.get(entry.getValue(), String.class));
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
            throw new UserInputException(String.format("Unexpected comment in 'SELECT' query.", violations));
        }
    }
}

