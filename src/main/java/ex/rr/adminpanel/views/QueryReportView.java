package ex.rr.adminpanel.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.exceptions.UserInputException;
import ex.rr.adminpanel.layouts.MainLayout;
import ex.rr.adminpanel.services.QueryService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
@Route(value = "queryReport", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class QueryReportView extends VerticalLayout {

    @Autowired
    QueryService queryService;

    QueryReportView() {
        Accordion reportCriteria = new Accordion();
        reportCriteria.setWidthFull();

        TextArea reportQuery = new TextArea("Query");
        reportQuery.setWidthFull();
        reportQuery.setHeight("20em");
        reportQuery.blur();

        Button button = new Button("Execute", event -> {
            try {
                add(queryService.withQuery(reportQuery.getValue()).toGrid());
                reportCriteria.close();
            } catch (PersistenceException e) {
                log.error(e.getMessage());
                displayError(NotificationVariant.LUMO_ERROR, "Failed to execute query.", e.getMessage());
            } catch (UserInputException e) {
                log.error(e.getMessage());
                String user = StringUtils.capitalize(SecurityContextHolder.getContext().getAuthentication().getName());
                displayError(NotificationVariant.LUMO_ERROR, e.getMessage(), user + "... You're on Santa's naughty list.", "Account suspended.");
            }
        });

        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.setWidthFull();
        criteriaLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        criteriaLayout.add(reportQuery);
        criteriaLayout.add(button);
        reportCriteria.add("Report criteria", criteriaLayout);

        add(reportCriteria);

    }

    private void displayError(NotificationVariant theme, String... messageLines) {
        Notification notification = new Notification();
        notification.addThemeVariants(theme);

        VerticalLayout vLayout = new VerticalLayout();
        for (String s : messageLines) {
            vLayout.add(new Div(new Text(s)));
        }

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.getElement().setAttribute("aria-label", "Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout hLayout = new HorizontalLayout(vLayout, closeButton);
        hLayout.setAlignItems(Alignment.CENTER);

        notification.add(hLayout);
        notification.open();
    }


}
