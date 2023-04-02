package ex.rr.adminpanel.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ex.rr.adminpanel.database.Trigger;
import ex.rr.adminpanel.enums.ActionType;
import ex.rr.adminpanel.enums.InputType;
import ex.rr.adminpanel.forms.TriggerForm;
import ex.rr.adminpanel.layouts.MainLayout;
import ex.rr.adminpanel.services.TriggerService;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Route(value = "triggersV2", layout = MainLayout.class)
@PageTitle("Triggers")
@RolesAllowed("ADMIN")
public class TriggersViewV2 extends VerticalLayout {

    private final TriggerService triggerService;

    private Dialog dialog;
    private Grid<Trigger> grid;
    //    private Editor<Trigger> editor;
    private ValidationMessage validationMessage;

    TriggersViewV2(TriggerService triggerService) {
        this.triggerService = triggerService;

        setAlignItems(Alignment.CENTER);
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("80%");

        layout.add(new H2("Inline editing"));
        String html = """
                I wasted time, so you don't have to.<br>
                Lombok '@Data' annotation doesn't work with inline editing.<br>
                Data is saved but editor won't close.<br>
                Regular getters and setters are required for this to work properly.
                """;
        Span span = new Span();
        span.getElement().setProperty("innerHTML", html);
        layout.add(span);

        Button addTrigger = new Button("Add Trigger");
        addTrigger.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addTrigger.addClickListener(event -> editTrigger());
        HorizontalLayout header = new HorizontalLayout(addTrigger);
        header.getStyle().set("flex-wrap", "wrap");
        header.setJustifyContentMode(JustifyContentMode.END);

        configureGrid();

        layout.setAlignItems(Alignment.STRETCH);
        layout.add(header, grid, validationMessage);
        add(layout);
    }

    private void configureGrid() {
        grid = new Grid<>(Trigger.class, false);
        Editor<Trigger> editor = grid.getEditor();
        validationMessage = new ValidationMessage();

        Grid.Column<Trigger> queryCol = grid.addColumn(Trigger::getInput).setHeader("Query").setFlexGrow(6);
        Grid.Column<Trigger> inputTypeCol = grid.addColumn(Trigger::getInputType).setHeader("Input Type").setFlexGrow(1);
        Grid.Column<Trigger> actionTypeCol = grid.addColumn(Trigger::getActionType).setHeader("Action Type").setFlexGrow(1);
        Grid.Column<Trigger> triggerCol = grid.addColumn(Trigger::getCron).setHeader("Cron").setFlexGrow(1);
        Grid.Column<Trigger> enabledCol = grid.addColumn(Trigger::getEnabled).setHeader("Enabled").setFlexGrow(0);
        Grid.Column<Trigger> editCol = grid.addComponentColumn(trigger -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(trigger);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);

        Binder<Trigger> binder = new Binder<>(Trigger.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField queryField = new TextField();
        queryField.setWidthFull();
        binder.forField(queryField)
                .asRequired("Field must not be empty")
                .withStatusLabel(validationMessage)
                .bind(Trigger::getInput, Trigger::setInput);
        queryCol.setEditorComponent(queryField);

        ComboBox<InputType> inputTypeField = new ComboBox<>();
        inputTypeField.setItems(InputType.values());
        inputTypeField.setWidthFull();
        binder.forField(inputTypeField)
                .asRequired("Field must not be empty")
                .withStatusLabel(validationMessage)
                .bind(Trigger::getInputType, Trigger::setInputType);
        inputTypeCol.setEditorComponent(inputTypeField);

        ComboBox<ActionType> actionTypeField = new ComboBox<>();
        actionTypeField.setItems(ActionType.values());
        actionTypeField.setWidthFull();
        binder.forField(actionTypeField)
                .asRequired("Field must not be empty")
                .withStatusLabel(validationMessage)
                .bind(Trigger::getActionType, Trigger::setActionType);
        actionTypeCol.setEditorComponent(actionTypeField);

        TextField cronField = new TextField();
        cronField.setWidthFull();
        binder.forField(cronField)
                .asRequired("Field must not be empty")
                .withStatusLabel(validationMessage)
                .bind(Trigger::getCron, Trigger::setCron);
        triggerCol.setEditorComponent(cronField);

        ComboBox<Boolean> enabledField = new ComboBox<>();
        enabledField.setItems(List.of(true, false));
        binder.forField(enabledField)
                .asRequired("Field must not be empty")
                .bind(Trigger::getEnabled, Trigger::setEnabled);
        enabledCol.setEditorComponent(enabledField);

        editor.addSaveListener(editorSaveEvent -> triggerService.save(editor.getItem()));

        Button saveButton = new Button("Save", e -> editor.save());
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton);
        actions.setPadding(false);
        editCol.setEditorComponent(actions);

        editor.addCancelListener(e -> validationMessage.setText(""));

        updateGrid();

        getThemeList().clear();
        getThemeList().add("spacing-s");
    }

    private void updateGrid() {
        grid.setItems(triggerService.findAll());
    }


    private void editTrigger() {
        dialog = new Dialog();
        dialog.setHeaderTitle("Trigger");

        TriggerForm form = new TriggerForm();
        form.setTrigger(new Trigger());
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
