package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.services.QueryResultSetService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ex.rr.adminpanel.ui.utils.Utils.displayNotification;

@Slf4j
@Route(value = "tableReport", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class TableReportView extends VerticalLayout {

    private static final String TABLES_QUERY = "SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA<>'INFORMATION_SCHEMA'";
    private static final String COLUMNS_QUERY = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='${SCHEMA}' AND TABLE_NAME='${TABLE}';";

    @Autowired
    QueryResultSetService queryService;

    private final Accordion reportCriteria;
    private final Accordion tables;
    private final VerticalLayout colLayout;
    private final VerticalLayout reportLayout;
    private Grid<HashMap<String, Object>> tableGrid;
    private Grid<HashMap<String, Object>> columnGrid;

    TableReportView() {
        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        reportCriteria = new Accordion();
        reportCriteria.setWidthFull();

        tables = new Accordion();
        tables.setWidthFull();
        Accordion columns = new Accordion();
        columns.setWidthFull();
        colLayout = new VerticalLayout();
        columns.add("Columns", colLayout);

        reportLayout = new VerticalLayout();

        Button button = new Button("Generate Report", event -> generateReport());

        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.setWidthFull();
        criteriaLayout.setAlignItems(Alignment.CENTER);
        criteriaLayout.add(tables, columns);
        criteriaLayout.add(button);
        reportCriteria.add("Report criteria", criteriaLayout);

        layout.add(reportCriteria);
        layout.add(reportLayout);
        add(layout);
    }

    private void generateReport() {
        reportLayout.removeAll();

        Set<HashMap<String, Object>> rows = tableGrid.getSelectedItems();
        Map<String, Object> queryParams = new HashMap<>();
        rows.forEach(t -> {
            String schemaCol = t.keySet().stream().filter(s -> s.contains("SCHEMA")).findFirst().orElseThrow();
            String tableCol = t.keySet().stream().filter(s -> s.contains("TABLE") && !s.equals(schemaCol)).findFirst().orElseThrow();
            queryParams.put("TABLE", String.format("%s.%s",
                    t.get(schemaCol).toString(),
                    t.get(tableCol).toString()));
        });
        queryParams.put("COLUMNS", columnGrid.getSelectedItems().stream().map(t ->
                t.get("COLUMN_NAME").toString()).toList());

        String query = String.format("SELECT %s FROM %s",
                String.join(", ", (List<String>) queryParams.get("COLUMNS")),
                queryParams.get("TABLE"));
        displayNotification(NotificationVariant.LUMO_PRIMARY, query);

        try {
            reportLayout.add(queryService.query(query, null, Grid.SelectionMode.NONE));
            reportCriteria.close();
        } catch (IndexOutOfBoundsException e) {
            displayNotification(NotificationVariant.LUMO_ERROR, "Query returned 0 rows.");
        } catch (Exception e) {
            displayNotification(NotificationVariant.LUMO_ERROR, "?");
        }
    }

    private void fetchColumns(String schemaName, String tableName) {
        colLayout.removeAll();
        colLayout.add(columnGrid = queryService
                .query(COLUMNS_QUERY
                                .replace("${SCHEMA}", schemaName)
                                .replace("${TABLE}", tableName)
                        , null
                        , Grid.SelectionMode.MULTI));
    }

    @PostConstruct
    private void loadData() {
        tables.add("Tables", tableGrid = queryService.query(TABLES_QUERY, null, Grid.SelectionMode.SINGLE));

        tableGrid.addSelectionListener(e -> {
            Set<HashMap<String, Object>> rows = tableGrid.getSelectedItems();
            rows.forEach(r ->
            {
                String schemaCol = r.keySet().stream().filter(s -> s.contains("SCHEMA")).findFirst().orElseThrow();
                String tableCol = r.keySet().stream().filter(s -> s.contains("TABLE") && !s.equals(schemaCol)).findFirst().orElseThrow();
                String schemaName = r.get(schemaCol).toString();
                String tableName = r.get(tableCol).toString();
                fetchColumns(schemaName, tableName);
            });
        });
    }

}
