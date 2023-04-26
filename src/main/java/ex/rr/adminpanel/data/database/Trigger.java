package ex.rr.adminpanel.data.database;

import ex.rr.adminpanel.data.annotations.Cron;
import ex.rr.adminpanel.data.enums.ActionType;
import ex.rr.adminpanel.data.enums.Env;
import ex.rr.adminpanel.data.enums.InputType;
import ex.rr.adminpanel.data.scheduler.TaskDefinition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The {@code Trigger} class represents the trigger database entity.
 * <ul>
 *     <li>{@code id} - Autogenerated id.</li>
 *     <li>{@code input} - Instructions used to fetch results.</li>
 *     <li>{@code inputType} - How the data will be retrieved.
 *         <pre>Values: QUERY, CURL, TEXT</pre></li>
 *     <li>{@code actionType} - Action to be executed when task returns data.
 *         <pre>Values: LOG, TEAMS_MESSAGE, REST</pre></li>
 *     <li>{@code cron} - Cron expression used for task execution</li>
 *     <li>{@code enabled} - TaskDefinition won't be scheduled if set to false</li>
 * </ul>
 * <p><i>Note: getters/setters intentionally created without Lombok.@Data to work with Vaadins Binder</i></p>
 *
 * @author rromanowicz
 * @see InputType
 * @see ActionType
 * @see TaskDefinition
 */
@Entity
@Table(name = "T_TRIGGER")
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Env env;
    @NotEmpty
    private String input;
    @NotNull
    private InputType inputType;
    @NotNull
    private ActionType actionType;
    @NotEmpty
    @Cron
    private String cron;
    @NotNull
    private Boolean enabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
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
