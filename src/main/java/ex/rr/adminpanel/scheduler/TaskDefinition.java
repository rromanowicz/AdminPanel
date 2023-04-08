package ex.rr.adminpanel.scheduler;

import ex.rr.adminpanel.database.Trigger;
import ex.rr.adminpanel.enums.ActionType;
import ex.rr.adminpanel.enums.InputType;
import lombok.Builder;
import lombok.Data;

/**
 * The {@code TaskDefinition} class represents definition of the task to be executed
 * <ul>
 *     <li>{@code id} - Name to be displayed.</li>
 *     <li>{@code cronExpression} - Cron expression used for task execution</li>
 *     <li>{@code inputType} - How the data will be retrieved.
 *         <pre>Values: QUERY, CURL, TEXT</pre></li>
 *     <li>{@code actionType} - Action to be executed when task returns data.
 *         <pre>Values: LOG, TEAMS_MESSAGE, REST</pre></li>
 *     <li>{@code data} - Instructions used to fetch results.</li>
 * </ul>
 *
 * @author Robert Romanowicz
 * @see InputType
 * @see ActionType
 */
@Data
@Builder
public class TaskDefinition {
    private String id;
    private String cronExpression;
    private InputType inputType;
    private ActionType actionType;
    private String data;

    /**
     * @param trigger {@code Trigger} entity
     * @return        {@code TaskDefinition}
     *
     * @see Trigger
     */
    public static TaskDefinition fromTrigger(Trigger trigger) {
        return TaskDefinition.builder()
                .id(trigger.getId())
                .cronExpression(trigger.getCron())
                .inputType(trigger.getInputType())
                .actionType(trigger.getActionType())
                .data(trigger.getInput())
                .build();
    }
}
