package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.models.templates.page.PageTemplate;
import ex.rr.adminpanel.services.TemplateService;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

@Route(value = "report", layout = MainLayout.class)
@PageTitle("Report")
@RolesAllowed({"REPORTS", "ADMIN"})
public class ReportView extends VerticalLayout {

    @Autowired
    private TemplateService templateService;

    ReportView() {
        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        Accordion reportCriteria = new Accordion();

        NumberField templateId = new NumberField("Template Id", "1");


        Button button = new Button("Select", event -> {
            reportCriteria.close();
            PageTemplate templateById = templateService.getTemplateById(1L);

            getTestGrid();
        });


        VerticalLayout criteriaLayout = new VerticalLayout();
        criteriaLayout.add(templateId);
        criteriaLayout.add(button);

        reportCriteria.add("Report criteria", criteriaLayout);
        layout.add(reportCriteria);
        add(layout);
    }

    private void getTestGrid() {

        Grid<Test> testGrid = new Grid<>(Test.class, true);
        testGrid.addColumn(Test::Col1).setHeader("1").setAutoWidth(true);
        testGrid.addColumn(Test::Col2).setHeader("2").setAutoWidth(true);
        testGrid.addColumn(Test::Col3).setHeader("3").setAutoWidth(true);
        testGrid.addColumn(Test::Col4).setHeader("4").setAutoWidth(true);
        testGrid.setItems(randomData());
        add(testGrid);
    }

    private Collection<Test> randomData() {
        return List.of(
                new Test("test1", "zxc1", 15, false),
                new Test("test1", "zxc1", 15, false),
                new Test("test1", "zxc1", 15, false),
                new Test("test1", "zxc1", 15, false),
                new Test("test1", "zxc1", 15, false),
                new Test("test1", "zxc1", 15, false)
        );
    }

    private List<String> reportParams(String reportType) {
        return switch (reportType) {
            case "Asd" -> List.of("1", "2");
            case "Zxc" -> List.of("3", "4");
            case "Qwe" -> List.of("5", "6");
            default -> List.of();
        };
    }


}

record Test(String Col1, String Col2, int Col3, boolean Col4) {
}
