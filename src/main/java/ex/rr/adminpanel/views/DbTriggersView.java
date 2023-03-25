package ex.rr.adminpanel.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.database.DbTrigger;
import ex.rr.adminpanel.layouts.MainLayout;
import ex.rr.adminpanel.services.TriggerService;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "dbTriggers", layout = MainLayout.class)
@PageTitle("DB Triggers")
@RolesAllowed("ADMIN")
public class DbTriggersView extends VerticalLayout {

    private TriggerService triggerService;

    DbTriggersView(TriggerService triggerService) {
        this.triggerService = triggerService;

        add(new H2("Popup editing"));

        Button addTrigger = new Button("Add Trigger");
        addTrigger.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTrigger.addClickListener(event -> editTrigger(null).open());
        HorizontalLayout header = new HorizontalLayout(addTrigger);
        header.getStyle().set("flex-wrap", "wrap");
        header.setJustifyContentMode(JustifyContentMode.END);

        ValidationMessage validationMessage = new ValidationMessage();

        Grid<DbTrigger> grid = new Grid<>(DbTrigger.class, false);

        grid.addColumn(DbTrigger::getQuery).setHeader("Query");
        grid.addColumn(DbTrigger::getType).setHeader("Type").setFlexGrow(0);
        grid.addColumn(DbTrigger::getTrigger).setHeader("Trigger").setFlexGrow(0);
        grid.addColumn(DbTrigger::getEnabled).setHeader("Enabled").setFlexGrow(0);
        grid.addComponentColumn(dbTrigger -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> editTrigger(dbTrigger).open());
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        List<DbTrigger> triggers = triggerService.getDbTriggers();
        grid.setItems(triggers);

        getThemeList().clear();
        getThemeList().add("spacing-s");

        setAlignItems(Alignment.STRETCH);
        add(header, grid, validationMessage);
    }


    private Dialog editTrigger(DbTrigger dbTrigger) {
        DbTrigger tempDbTrigger = (dbTrigger == null) ? new DbTrigger() : dbTrigger;
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Trigger");

        TextField type = new TextField("Trigger", "", "");
        TextField trigger = new TextField("Trigger", "", "");
        ComboBox<Boolean> enabled = new ComboBox<>("Enabled", List.of(true, false));
        TextArea query = new TextArea();
        query.setLabel("Query");
        FormLayout formLayout = new FormLayout(type, trigger, enabled, query);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
        formLayout.setColspan(query, 3);

        if (dbTrigger != null) {
            type.setValue(tempDbTrigger.getType());
            trigger.setValue(tempDbTrigger.getTrigger());
            enabled.setValue(tempDbTrigger.getEnabled());
            query.setValue(tempDbTrigger.getQuery());
        }


        Button delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-inline-end", "auto");

        Button cancel = new Button("Cancel");
        cancel.addClickListener(e -> {
            UI.getCurrent().getPage().reload();
            dialog.close();
        });

        Button save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(e -> {
            tempDbTrigger.setQuery(query.getValue());
            tempDbTrigger.setType(type.getValue());
            tempDbTrigger.setTrigger(trigger.getValue());
            tempDbTrigger.setEnabled(enabled.getValue());
            triggerService.save(tempDbTrigger);
            UI.getCurrent().getPage().reload();
            dialog.close();
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(delete, cancel, save);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        setPadding(false);
        setAlignItems(Alignment.STRETCH);
        dialog.add(formLayout, buttonLayout);

        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center").set("opacity", "0");

        return dialog;
    }

}
