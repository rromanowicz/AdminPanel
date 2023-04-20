package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.data.services.QueryResultSetService;
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

    public static final String TBL = "TBL";
    public static final String SCH = "SCH";
    public static final String COL = "COL";

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
        rows.forEach(t -> queryParams.put("TABLE", String.format("%s.%s",
                t.get(SCH).toString(),
                t.get(TBL).toString())));
        queryParams.put("COLUMNS", columnGrid.getSelectedItems().stream().map(t ->
                t.get(COL).toString()).toList());

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
        colLayout.add(columnGrid = queryService.getTableColumns(schemaName, tableName));
    }

    @PostConstruct
    private void loadData() {
        tables.add("Tables", tableGrid = queryService.getTables());// query(TABLES_QUERY, null, Grid.SelectionMode.SINGLE));

        tableGrid.addSelectionListener(e -> {
            Set<HashMap<String, Object>> rows = tableGrid.getSelectedItems();
            rows.forEach(r ->
            {
                String schemaName = r.get(SCH).toString();
                String tableName = r.get(TBL).toString();
                fetchColumns(schemaName, tableName);
            });
        });
    }

}
