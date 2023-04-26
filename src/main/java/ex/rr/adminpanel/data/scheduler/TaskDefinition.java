package ex.rr.adminpanel.data.scheduler;

import ex.rr.adminpanel.data.database.Trigger;
import ex.rr.adminpanel.data.enums.ActionType;
import ex.rr.adminpanel.data.enums.InputType;
import lombok.Builder;
import lombok.Data;

/**
 * The {@code TaskDefinition} class represents definition of the task to be executed
 * <ul>
 *     <li>{@code id} - Id of the stored Trigger.</li>
 *     <li>{@code cronExpression} - Cron expression used for task execution</li>
 *     <li>{@code inputType} - How the data will be retrieved.
 *         <pre>Values: QUERY, CURL, TEXT</pre></li>
 *     <li>{@code actionType} - Action to be executed when task returns data.
 *         <pre>Values: LOG, TEAMS_MESSAGE, REST</pre></li>
 *     <li>{@code data} - Instructions used to fetch results.</li>
 * </ul>
 *
 * @author  rromanowicz
 * @see     InputType
 * @see     ActionType
 * @see     Trigger
 */
@Data
@Builder
public class TaskDefinition {
    private String id;
    private String cronExpression;
    private InputType inputType;
    private ActionType actionType;
    private boolean cyclic;
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
