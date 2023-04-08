package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
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
import com.vaadin.flow.server.VaadinSession;
import ex.rr.adminpanel.ui.handler.SimpleErrorHandler;
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(value = "report", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class ReportView extends VerticalLayout {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private QueryService queryService;


    private final ComboBox<String> report;
    private final VerticalLayout mainLayout;
    private String filterCriteria;
    private Map<String, Filter> filterMap;
    private VerticalLayout filterLayout;
    private PageTemplate template;

    protected void onAttach(AttachEvent attachEvent) {
        report.setItems(templateService.getTemplateList());
        filterMap = new HashMap<>();

        VaadinSession.getCurrent().setErrorHandler(new SimpleErrorHandler());
    }

    ReportView() {
        mainLayout = new VerticalLayout();
        mainLayout.setWidthFull();
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
        report.setPlaceholder("Select report");
        report.getStyle().set("--vaadin-combo-box-overlay-width", "30%");


        Button button = new Button("Select", event -> {
            reportCriteria.close();
            template = templateService.getTemplateByName(report.getValue());

            VerticalLayout generatedLayout = generateView(template);
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
        mainLayout.removeAll();
        mainLayout.add(new H1(template.getName()));

        Accordion accordion = new Accordion();
        accordion.setWidthFull();
        accordion.close();

        AccordionPanel filterPanel = accordion.add("Filters", getGlobalFilters(template.getGlobalFilters()));
        filterPanel.setOpened(false);

        template.getSections().forEach(section -> {
            switch (section.getType()) {
                case REPORT -> {
                    AccordionPanel reportPanel = accordion.add(section.getName(), getReport(section));
                    reportPanel.setOpened(true);
                }
                case DATA_GRID -> {
                    AccordionPanel dataPanel = accordion.add(section.getName(), getDataGrid(section));
                    dataPanel.setOpened(false);
                }
            }
        });

        accordion.open(1);
        mainLayout.add(accordion);

        return mainLayout;
    }


    @NotNull
    private VerticalLayout getGlobalFilters(List<Filter> filters) {
        if (filterLayout == null) {
            filterLayout = new VerticalLayout();
            filters.forEach(filter -> {
                HorizontalLayout hl = new HorizontalLayout();
                hl.setAlignItems(Alignment.CENTER);
                hl.add(getFilterComponent(filter));
                filterLayout.add(hl);
            });

            Button apply = new Button("Apply");
            apply.addClickListener(event -> {
                filterCriteria = buildFilterCriteria();
                generateView(template);
            });
            filterLayout.add(apply);
            filterCriteria = buildFilterCriteria();
        }

        return filterLayout;
    }

    private Component getFilterComponent(Filter filter) {
        switch (filter.getColumnType()) {
            case TEXT -> {
                TextField textField = new TextField(String.format("%s (%s)", filter.getColumn(), filter.getOperator()), filter.getValue());
                filterMap.put(filter.getColumn(), filter);
                textField.addValueChangeListener(event -> updateFilter(filter.getColumn(), textField.getValue()));
                return textField;
            }
            case NUMBER -> {
                NumberField numberField = new NumberField(String.format("%s (%s)", filter.getColumn(), filter.getOperator()), filter.getValue());
                filterMap.put(filter.getColumn(), filter);
                numberField.addValueChangeListener(event -> updateFilter(filter.getColumn(), String.valueOf(numberField.getValue())));
                return numberField;
            }
            case DATE -> {
                DatePicker datePicker = new DatePicker(String.format("%s (%s)", filter.getColumn(), filter.getOperator()), getDate(filter.getValue()));
                filterMap.put(filter.getColumn(), filter);
                datePicker.addValueChangeListener(event -> updateFilter(filter.getColumn(), datePicker.getValue().format(DateTimeFormatter.ISO_DATE)));
                return datePicker; //TODO get value from Filter
            }
            case DATETIME -> {
                DateTimePicker dateTimePicker = new DateTimePicker(String.format("%s (%s)", filter.getColumn(), filter.getOperator()), getDateTime(filter.getValue()));
                filterMap.put(filter.getColumn(), filter);
                dateTimePicker.addValueChangeListener(event -> updateFilter(filter.getColumn(), dateTimePicker.getValue().format(DateTimeFormatter.ISO_DATE)));
                return dateTimePicker; //TODO get value from Filter
            }
            default -> {
                return null;
            }
        }

    }

    private void updateFilter(String column, String value) {
        Filter filter = filterMap.get(column);
        filter.setValue(value);
        filterMap.put(column, filter);
    }

    @NotNull
    private VerticalLayout getDataGrid(PageSection section) {
        return getView(queryService.withQuery(section.getQuery()).withActions(section.getDataGrid().getActions()).toGrid(Grid.SelectionMode.NONE));
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

        Accordion reportAccordion = new Accordion();
        reportAccordion.setWidthFull();

        VerticalLayout criteriaLayout = new VerticalLayout();
        VerticalLayout reportLayout = new VerticalLayout();

        Grid<String> dimensions = getSelectionGrid(section.getReport().getDimensions(), section.getReport().getInitialReport().getDimensions());
        Grid<String> facts = getSelectionGrid(section.getReport().getFacts(), section.getReport().getInitialReport().getFacts());

        HorizontalLayout criteriaHL = new HorizontalLayout();
        criteriaHL.setWidthFull();
        criteriaHL.add(dimensions, facts);

        Button refresh = new Button("Refresh");
        refresh.addClickListener(event -> {
            String query = getQuery(section, dimensions, facts);
            reportLayout.removeAll();
            reportLayout.add(getView(queryService.withQuery(query).toGrid(Grid.SelectionMode.NONE)));
            reportAccordion.open(1);
        });

        criteriaLayout.add(criteriaHL, refresh);

        reportAccordion.add(new AccordionPanel("Criteria", criteriaLayout));
        reportLayout.add(getView(queryService.withQuery(getQuery(section, dimensions, facts)).toGrid(Grid.SelectionMode.NONE)));
        reportAccordion.add("Report", reportLayout);

        reportAccordion.open(1);
        vl.add(reportAccordion);

        return vl;
    }

    private String getQuery(PageSection section, Grid<String> dimensions, Grid<String> facts) {
        String query;
        String selectedDimensions = String.join(", ", dimensions.getSelectedItems());
        String selectedFacts = String.join(", ", facts.getSelectedItems().stream().map(it -> String.format("SUM(%s)", it)).toList());
        if (selectedDimensions.isEmpty()) {
            query = String.format("SELECT %s FROM (%s) WHERE %s %s",
                    selectedFacts,
                    section.getQuery().replace(";", ""),
                    "1=1", filterCriteria
            );
        } else {
            query = String.format("SELECT %s FROM (%s) WHERE %s %s GROUP BY %s ORDER BY %s",
                    String.join(", ", selectedDimensions, selectedFacts),
                    section.getQuery().replace(";", ""),
                    "1=1", filterCriteria,
                    selectedDimensions,
                    selectedDimensions
            );
        }
        return query;
    }

    private String buildFilterCriteria(List<Filter> filters) {
        StringBuilder str = new StringBuilder();
        filters.forEach(filter -> {
            String operator = switch (filter.getOperator()) {
                case EQUAL -> "=";
                case NOT_EQUAL -> "<>";
                case GREATER_THAN -> ">";
                case LESS_THAN -> "<";
                case GREATER_THAN_OR_EQUAL -> ">=";
                case LESS_THAN_OR_EQUAL -> "<=";
            };
            str.append("AND ").append(switch (filter.getColumnType()) {
                case TEXT -> String.format("%s%s'%s'", filter.getColumn(), operator, filter.getValue());
                case NUMBER -> String.format("%s%s%s", filter.getColumn(), operator, filter.getValue());
                case DATE ->
                        String.format("%s%s'%s'", filter.getColumn(), operator, getDate(filter.getValue()).format(DateTimeFormatter.ISO_DATE));
                case DATETIME ->
                        String.format("%s%s'%s'", filter.getColumn(), operator, getDateTime(filter.getValue()).format(DateTimeFormatter.ISO_DATE));
            });
        });
        return str.toString();
    }

    private String buildFilterCriteria() {
        StringBuilder str = new StringBuilder();
        filterMap.forEach((key, filter) -> {
            String operator = switch (filter.getOperator()) {
                case EQUAL -> "=";
                case NOT_EQUAL -> "<>";
                case GREATER_THAN -> ">";
                case LESS_THAN -> "<";
                case GREATER_THAN_OR_EQUAL -> ">=";
                case LESS_THAN_OR_EQUAL -> "<=";
            };
            str.append("AND ").append(switch (filter.getColumnType()) {
                case TEXT -> String.format("%s%s'%s'", key, operator, filter.getValue());
                case NUMBER -> String.format("%s%s%s", key, operator, filter.getValue());
                case DATE ->
                        String.format("%s%s'%s'", key, operator, getDate(filter.getValue()).format(DateTimeFormatter.ISO_DATE));
                case DATETIME ->
                        String.format("%s%s'%s'", key, operator, getDateTime(filter.getValue()).format(DateTimeFormatter.ISO_DATE));
            });
        });
        return str.toString();
    }


    private LocalDate getDate(String value) {
        if ("TODAY".equals(value)) {
            return LocalDate.now();
        }
        if (value.contains("TODAY")) {
            return LocalDate.now().plusDays(Long.parseLong(value.replace("TODAY", "")));
        } else {
            return LocalDate.parse(value);
        }
    }

    private LocalDateTime getDateTime(String value) {
        if ("TODAY".equals(value)) {
            return LocalDateTime.now();
        } else if (value.contains("TODAY")) {
            return LocalDateTime.now().plusDays(Long.parseLong(value.replace("TODAY", "")));
        } else {
            return LocalDateTime.parse(value);
        }
    }

    @NotNull
    private VerticalLayout getView(Component component) {
        VerticalLayout sectionLayout = new VerticalLayout();
        sectionLayout.setWidthFull();
        sectionLayout.add(component);
        return sectionLayout;
    }


}
