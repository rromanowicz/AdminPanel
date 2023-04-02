package ex.rr.adminpanel.database;

import ex.rr.adminpanel.annotations.Cron;
import ex.rr.adminpanel.enums.ActionType;
import ex.rr.adminpanel.enums.InputType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TRIGGER")
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @NotEmpty
    String input;
    @NotNull
    InputType inputType;
    @NotNull
    ActionType actionType;
    @NotEmpty
    @Cron
    String cron;
    @NotNull
    Boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
