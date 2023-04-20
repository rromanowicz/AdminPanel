package ex.rr.adminpanel.ui.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ex.rr.adminpanel.data.database.Trigger;
import ex.rr.adminpanel.data.enums.ActionType;
import ex.rr.adminpanel.data.enums.InputType;
import lombok.Data;

import java.util.List;

@Data
public class TriggerForm extends FormLayout {

    ComboBox<InputType> inputType = new ComboBox<>("Input Type", InputType.values());
    ComboBox<ActionType> actionType = new ComboBox<>("Action Type", ActionType.values());
    TextField cron = new TextField("Cron", "", "");
    ComboBox<Boolean> enabled = new ComboBox<>("Enabled", List.of(true, false));
    TextArea input = new TextArea("Input");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Trigger> binder = new BeanValidationBinder<>(Trigger.class);
    private Trigger trigger;

    public TriggerForm() {

        addClassName("trigger-form");
        binder.bindInstanceFields(this);

        this.add(inputType, actionType, cron, enabled, input, createButtonsLayout());
        this.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 3));
        this.setColspan(input, 3);
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
        binder.readBean(trigger);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, trigger)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(trigger);
            fireEvent(new SaveEvent(this, trigger));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class TriggerFormEvent extends ComponentEvent<TriggerForm> {
        private Trigger trigger;

        protected TriggerFormEvent(TriggerForm source, Trigger trigger) {
            super(source, false);
            this.trigger = trigger;
        }

        public Trigger getTrigger() {
            return trigger;
        }
    }

    public static class SaveEvent extends TriggerFormEvent {
        SaveEvent(TriggerForm source, Trigger trigger) {
            super(source, trigger);
        }
    }

    public static class DeleteEvent extends TriggerFormEvent {
        DeleteEvent(TriggerForm source, Trigger trigger) {
            super(source, trigger);
        }
    }

    public static class CloseEvent extends TriggerFormEvent {
        CloseEvent(TriggerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}


