package ex.rr.adminpanel.scheduler;

import ex.rr.adminpanel.database.Trigger;
import ex.rr.adminpanel.enums.ActionType;
import ex.rr.adminpanel.enums.InputType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDefinition {
    private String cronExpression;
    private InputType inputType;
    private ActionType actionType;
    private String data;

    public static TaskDefinition fromTrigger(Trigger trigger) {
        return TaskDefinition.builder()
                .cronExpression(trigger.getCron())
                .inputType(trigger.getInputType())
                .actionType(trigger.getActionType())
                .data(trigger.getInput())
                .build();
    }
}
