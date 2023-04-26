package ex.rr.adminpanel.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.data.database.Trigger;
import ex.rr.adminpanel.ui.forms.TriggerForm;
import ex.rr.adminpanel.ui.layouts.MainLayout;
import ex.rr.adminpanel.data.services.TriggerService;
import jakarta.annotation.security.RolesAllowed;

@Route(value = "triggers", layout = MainLayout.class)
@PageTitle("Triggers")
@RolesAllowed("ADMIN")
public class TriggersView extends VerticalLayout {

    private final TriggerService triggerService;

    private final Grid<Trigger> grid = new Grid<>(Trigger.class, false);
    private Dialog dialog;

    TriggersView(TriggerService triggerService) {
        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        this.triggerService = triggerService;

        layout.add(new H2("Popup editing"));

        Button addTrigger = new Button("Add Trigger");
        addTrigger.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTrigger.addClickListener(event -> editTrigger(null));
        HorizontalLayout header = new HorizontalLayout(addTrigger);
        header.getStyle().set("flex-wrap", "wrap");
        header.setJustifyContentMode(JustifyContentMode.END);

        configureGrid();
        updateGrid();

        getThemeList().clear();
        getThemeList().add("spacing-s");

        layout.setAlignItems(Alignment.STRETCH);
        layout.add(header, grid);
        add(layout);
    }


    private void configureGrid() {
        grid.addColumn(Trigger::getEnv).setHeader("Env").setFlexGrow(1);
        grid.addColumn(Trigger::getInput).setHeader("Query").setFlexGrow(6);
        grid.addColumn(Trigger::getInputType).setHeader("Input Type").setFlexGrow(1);
        grid.addColumn(Trigger::getActionType).setHeader("Action Type").setFlexGrow(1);
        grid.addColumn(Trigger::getCron).setHeader("Cron").setFlexGrow(1);
        grid.addColumn(Trigger::getEnabled).setHeader("Enabled").setFlexGrow(0);
        grid.addComponentColumn(trigger -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> editTrigger(trigger));
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
    }

    private void updateGrid() {
        grid.setItems(triggerService.findAll());
    }


    private void editTrigger(Trigger trigger) {
        Trigger tempTrigger = (trigger == null) ? new Trigger() : trigger;
        dialog = new Dialog();
        dialog.setHeaderTitle("Trigger");

        TriggerForm form = new TriggerForm();
        form.setTrigger(tempTrigger);
        form.addListener(TriggerForm.SaveEvent.class, e -> {
            triggerService.save(e.getTrigger());
            updateGrid();
            dialog.close();
        });
        form.addListener(TriggerForm.DeleteEvent.class, e -> {
            triggerService.delete(e.getTrigger().getId());
            updateGrid();
            dialog.close();
        });
        form.addListener(TriggerForm.CloseEvent.class, e -> dialog.close());

        dialog.add(form);
        dialog.open();
    }

}
