package ex.rr.adminpanel.scheduler;

import ex.rr.adminpanel.enums.ActionType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskDefinition {
    private String cronExpression;
    private ActionType actionType;
    private String data;
}
