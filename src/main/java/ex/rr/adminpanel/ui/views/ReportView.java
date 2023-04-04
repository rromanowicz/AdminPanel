package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.models.templates.page.PageSection;
import ex.rr.adminpanel.models.templates.page.PageTemplate;
import ex.rr.adminpanel.services.QueryService;
import ex.rr.adminpanel.services.TemplateService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "report", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class ReportView extends VerticalLayout {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private QueryService queryService;


    ComboBox<String> report;

    protected void onAttach(AttachEvent attachEvent) {
        report.setItems(templateService.getTemplateList());
    }

    ReportView() {
        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        Accordion reportCriteria = new Accordion();

        Accordion generatedView = new Accordion();
        generatedView.setWidthFull();
        VerticalLayout generatedViewLayout = new VerticalLayout();
        generatedViewLayout.setWidthFull();
        generatedView.add("Report", generatedViewLayout);

        report = new ComboBox<>("Select report");


        Button button = new Button("Select", event -> {
            reportCriteria.close();
            PageTemplate templateById = templateService.getTemplateByName(report.getValue());

            VerticalLayout generatedLayout = generateView(templateById);
            generatedViewLayout.removeAll();
            generatedViewLayout.add(generatedLayout);
        });


        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.add(report);
        criteriaLayout.add(button);

        reportCriteria.add("Report criteria", criteriaLayout);
        layout.add(reportCriteria);
        layout.add(generatedView);
        add(layout);
    }

    private VerticalLayout generateView(PageTemplate template) {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthFull();
        layout.add(new H1(template.getName()));

        template.getSections().forEach(section -> {
            Accordion sectionAccordion = switch (section.getType()) {
                case REPORT -> getReport(section);
                case DATA_GRID -> getDataGrid(section);
            };

            layout.add(sectionAccordion);
        });

        return layout;
    }

    @NotNull
    private Accordion getDataGrid(PageSection section) {
        return getView(queryService.withQuery(section.getQuery()).withActions(section.getDataGrid().getActions()).toGrid(Grid.SelectionMode.NONE), section.getName());
    }

    @NotNull
    private Accordion getReport(PageSection section) {
        return getView(queryService.withQuery(section.getQuery()).toGrid(Grid.SelectionMode.NONE), section.getName());
    }

    @NotNull
    private Accordion getView(Component component, String name) {
        Accordion sectionAccordion = new Accordion();
        sectionAccordion.setWidthFull();
        VerticalLayout sectionLayout = new VerticalLayout();
        sectionLayout.setWidthFull();

        sectionLayout.add(component);

        sectionAccordion.add(name, sectionLayout);
        return sectionAccordion;
    }


}
