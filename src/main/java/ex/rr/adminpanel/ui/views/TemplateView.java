package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.data.database.Template;
import ex.rr.adminpanel.data.services.TemplateService;
import ex.rr.adminpanel.ui.forms.TemplateForm;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "templates", layout = MainLayout.class)
@PageTitle("Templates")
@RolesAllowed("ADMIN")
public class TemplateView extends VerticalLayout {

    private final TemplateService templateService;

    private final Grid<Template> grid = new Grid<>(Template.class, false);
    private Dialog dialog;

    TemplateView(TemplateService templateService) {
        setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        setSizeFull();
        expand(layout);
        expand(grid);

        this.templateService = templateService;

        Button addTrigger = new Button("Add Template");
        addTrigger.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTrigger.addClickListener(event -> editUser(null));
        HorizontalLayout header = new HorizontalLayout(addTrigger);
        header.getStyle().set("flex-wrap", "wrap");
        header.setJustifyContentMode(JustifyContentMode.END);

        configureGrid();
        updateGrid();

        layout.setAlignItems(Alignment.STRETCH);
        layout.add(header, grid);
        add(layout);
    }

    private void configureGrid() {
        grid.addColumn(Template::getId).setHeader("Id").setFlexGrow(0);
        grid.addColumn(Template::getTemplateJson).setHeader("Template").setFlexGrow(5);
        grid.addColumn(Template::isActive).setHeader("Enabled").setFlexGrow(0);
        grid.addComponentColumn(trigger -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> editUser(trigger));
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private void updateGrid() {
        grid.setItems(templateService.findAll());
    }

    private void editUser(Template template) {
        Template tempTemplate = (template == null) ? new Template() : template;
        dialog = new Dialog();
        dialog.setHeaderTitle("User");

        TemplateForm form = new TemplateForm();
        form.setPageTemplate(tempTemplate);
        form.addListener(TemplateForm.SaveEvent.class, e -> {
            templateService.saveTemplate(e.getTemplate());
            updateGrid();
            dialog.close();
        });
        form.addListener(TemplateForm.DeleteEvent.class, e -> {
            templateService.disable(e.getTemplate().getId());
            updateGrid();
            dialog.close();
        });
        form.addListener(TemplateForm.CloseEvent.class, e -> dialog.close());

        dialog.add(form);
        dialog.open();
    }

}
