package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.models.templates.page.Filter;
import ex.rr.adminpanel.models.templates.page.PageSection;
import ex.rr.adminpanel.models.templates.page.PageTemplate;
import ex.rr.adminpanel.services.QueryService;
import ex.rr.adminpanel.services.TemplateService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        layout.add(getGlobalFilters(template.getGlobalFilters()));

        template.getSections().forEach(section -> {
            switch (section.getType()) {
                case REPORT -> layout.add(getReport(section));
                case DATA_GRID -> layout.add(getDataGrid(section));
            }
        });

        return layout;
    }


    @NotNull
    private Accordion getGlobalFilters(List<Filter> filters) {
        Accordion globalFilters = new Accordion();
        VerticalLayout filterLayout = new VerticalLayout();
        filters.forEach(filter -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            hl.add(getFilterComponent(filter));
            filterLayout.add(hl);
        });

        globalFilters.add("Filters", filterLayout);
        return globalFilters;
    }

    private Component getFilterComponent(Filter filter) {
        return switch (filter.getColumnType()) {
            case TEXT -> new TextField(filter.getColumn(), filter.getValue());
            case NUMBER -> new NumberField(filter.getColumn(), filter.getValue());
            case DATE -> new DatePicker(filter.getColumn(), LocalDate.now());
            case DATETIME -> new DateTimePicker(filter.getColumn(), LocalDateTime.now());
        };
    }

    @NotNull
    private Accordion getDataGrid(PageSection section) {
        return getView(section.getName(), queryService.withQuery(section.getQuery()).withActions(section.getDataGrid().getActions()).toGrid(Grid.SelectionMode.NONE));
    }

    private Grid<String> getSelectionGrid(List<String> columns, List<String> selected) {
        Grid<String> grid = new Grid<>(String.class, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(s -> s).setHeader("Column Name");
        grid.setItems(columns);

        for (String s : selected) {
            grid.getSelectionModel().select(s);
        }

        return grid;
    }

    @NotNull
    private VerticalLayout getReport(PageSection section) {
        VerticalLayout vl = new VerticalLayout();

        VerticalLayout reportSection = new VerticalLayout();

        Accordion selection = new Accordion();
        selection.setWidthFull();
        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidthFull();

        Grid<String> dimensions = getSelectionGrid(section.getReport().getDimensions(), section.getReport().getInitialReport().getDimensions());
        Grid<String> facts = getSelectionGrid(section.getReport().getFacts(), section.getReport().getInitialReport().getFacts());

        hl.add(dimensions, facts);
        selection.add("Selection", hl);


        vl.add(selection);

        Button refresh = new Button("Refresh");
        refresh.addClickListener(event -> {
            String query = getQuery(section, dimensions, facts);
            reportSection.removeAll();
            reportSection.add(getView(section.getName(), queryService.withQuery(query).toGrid(Grid.SelectionMode.NONE)));
        });

        vl.add(refresh);

        Accordion reportGrid = getView(section.getName(), queryService.withQuery(getQuery(section, dimensions, facts)).toGrid(Grid.SelectionMode.NONE));

        reportSection.add(reportGrid);
        vl.add(reportSection);
        return vl;
    }

    private static String getQuery(PageSection section, Grid<String> dimensions, Grid<String> facts) {
        String query;
        String selectedDimensions = String.join(", ", dimensions.getSelectedItems());
        String selectedFacts = String.join(", ", facts.getSelectedItems().stream().map(it -> String.format("SUM(%s)", it)).toList());
        if (selectedDimensions.isEmpty()) {
            query = String.format("SELECT %s FROM (%s) WHERE %s",
                    selectedFacts,
                    section.getQuery().replace(";", ""),
                    "1=1" //FIXME
            );
        } else {
            query = String.format("SELECT %s FROM (%s) WHERE %s GROUP BY %s ORDER BY %s",
                    String.join(", ", selectedDimensions, selectedFacts),
                    section.getQuery().replace(";", ""),
                    "1=1", //FIXME
                    selectedDimensions,
                    selectedDimensions
            );
        }
        return query;
    }

    @NotNull
    private Accordion getView(String name, Component component) {
        Accordion sectionAccordion = new Accordion();
        sectionAccordion.setWidthFull();
        VerticalLayout sectionLayout = new VerticalLayout();
        sectionLayout.setWidthFull();

        sectionLayout.add(component);

        sectionAccordion.add(name, sectionLayout);
        return sectionAccordion;
    }


}
