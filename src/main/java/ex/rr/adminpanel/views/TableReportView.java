package ex.rr.adminpanel.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.layouts.MainLayout;
import ex.rr.adminpanel.services.QueryService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ex.rr.adminpanel.utils.Utils.displayNotification;

@Slf4j
@Route(value = "tableReport", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class TableReportView extends VerticalLayout {

    private static final String TABLES_QUERY = "SELECT TABLE_SCHEMA, TABLE_NAME, TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES";
    private static final String COLUMNS_QUERY = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='${SCHEMA}' AND TABLE_NAME='${TABLE}';";
    private static final Integer SCHEMA_NAME_INDEX = 0;
    private static final Integer TABLE_NAME_INDEX = 1;

    @Autowired
    QueryService queryService;

    private final Accordion reportCriteria;
    private final Accordion tables;
    private final VerticalLayout colLayout;
    private final VerticalLayout reportLayout;
    private Grid<Tuple> tableGrid;
    private Grid<Tuple> columnGrid;

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

        Button button = new Button("Generate Report", event -> {
            generateReport();
        });

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

        Set<Tuple> tuples = tableGrid.getSelectedItems();
        Map<String, Object> queryParams = new HashMap<>();
        tuples.forEach(t ->
                queryParams.put("TABLE", String.format("%s.%s",
                        t.get(t.getElements().get(SCHEMA_NAME_INDEX).getAlias()).toString(),
                        t.get(t.getElements().get(TABLE_NAME_INDEX).getAlias()).toString())));
        queryParams.put("COLUMNS", columnGrid.getSelectedItems().stream().map(t ->
                t.get(t.getElements().get(0).getAlias()).toString()).toList());

        String query = String.format("SELECT %s FROM %s",
                String.join(", ", (List<String>) queryParams.get("COLUMNS")),
                queryParams.get("TABLE"));
        displayNotification(NotificationVariant.LUMO_PRIMARY, query);

        try {
            reportLayout.add(queryService.withQuery(query).toGrid(null));
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
                .withQuery(COLUMNS_QUERY
                        .replace("${SCHEMA}", schemaName)
                        .replace("${TABLE}", tableName))
                .toGrid(Grid.SelectionMode.MULTI));
    }

    @PostConstruct
    private void loadData() {
        tables.add("Tables", tableGrid = queryService.withQuery(TABLES_QUERY).toGrid(Grid.SelectionMode.SINGLE));

        tableGrid.addSelectionListener(e -> {
            Set<Tuple> tuples = tableGrid.getSelectedItems();
            tuples.forEach(t ->
            {
                String schemaName = t.get(t.getElements().get(SCHEMA_NAME_INDEX).getAlias()).toString();
                String tableName = t.get(t.getElements().get(TABLE_NAME_INDEX).getAlias()).toString();
                fetchColumns(schemaName, tableName);
                String alias = String.format("%s.%s", schemaName, tableName);
            });
        });
    }

}
