package ex.rr.adminpanel.ui.forms;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ex.rr.adminpanel.database.Template;
import lombok.Data;

@Data
public class TemplateForm extends FormLayout {

    IntegerField templateId = new IntegerField("Id");
    TextArea template = new TextArea("Template", "", "");

    Checkbox active = new Checkbox("Active");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Template> binder = new BeanValidationBinder<>(Template.class);
    private Template pageTemplate;

    public TemplateForm() {
        templateId.setReadOnly(true);
        addClassName("trigger-form");
        binder.bindInstanceFields(this);

        this.add(templateId, template, active, createButtonsLayout());
    }

    public void setPageTemplate(Template pageTemplate) {
        this.pageTemplate = pageTemplate;
        binder.bind(templateId, "id");
        binder.readBean(pageTemplate);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, pageTemplate)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(pageTemplate);
            fireEvent(new SaveEvent(this, pageTemplate));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class TemplateFormEvent extends ComponentEvent<TemplateForm> {
        private Template pageTemplate;

        protected TemplateFormEvent(TemplateForm source, Template pageTemplate) {
            super(source, false);
            this.pageTemplate = pageTemplate;
        }

        public Template getTemplate() {
            return pageTemplate;
        }
    }

    public static class SaveEvent extends TemplateFormEvent {
        SaveEvent(TemplateForm source, Template pageTemplate) {
            super(source, pageTemplate);
        }
    }

    public static class DeleteEvent extends TemplateFormEvent {
        DeleteEvent(TemplateForm source, Template pageTemplate) {
            super(source, pageTemplate);
        }
    }

    public static class CloseEvent extends TemplateFormEvent {
        CloseEvent(TemplateForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}


