package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.data.exceptions.UserInputException;
import ex.rr.adminpanel.data.services.QueryResultSetService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static ex.rr.adminpanel.ui.utils.Utils.displayNotification;

@Slf4j
@Route(value = "queryReport", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class QueryReportView extends VerticalLayout {

    @Autowired
    QueryResultSetService queryService;


    QueryReportView() {
        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        Accordion reportCriteria = new Accordion();
        reportCriteria.setWidthFull();

        TextArea reportQuery = new TextArea("Query");
        reportQuery.setWidthFull();
        reportQuery.setHeight("20em");
        reportQuery.blur();

        Button button = new Button("Execute", event -> {
            try {
                add(queryService.query(reportQuery.getValue(), null, Grid.SelectionMode.NONE));
                reportCriteria.close();
            } catch (PersistenceException e) {
                log.error(e.getMessage());
                displayNotification(NotificationVariant.LUMO_ERROR, "Failed to execute query.", e.getMessage());
            } catch (UserInputException e) {
                log.error(e.getMessage());
                displayNotification(NotificationVariant.LUMO_ERROR, "?");
            } catch (IndexOutOfBoundsException e) {
                displayNotification(NotificationVariant.LUMO_ERROR, "Query returned 0 rows.");
            }
        });

        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.setWidthFull();
        criteriaLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        criteriaLayout.add(reportQuery);
        criteriaLayout.add(button);
        reportCriteria.add("Report criteria", criteriaLayout);

        layout.add(reportCriteria);
        add(layout);
    }

}
