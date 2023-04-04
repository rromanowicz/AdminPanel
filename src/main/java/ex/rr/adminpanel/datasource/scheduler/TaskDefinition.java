package ex.rr.adminpanel.datasource.scheduler;

import ex.rr.adminpanel.datasource.database.Trigger;
import ex.rr.adminpanel.datasource.enums.ActionType;
import ex.rr.adminpanel.datasource.enums.InputType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDefinition {
    private String id;
    private String cronExpression;
    private InputType inputType;
    private ActionType actionType;
    private String data;

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
