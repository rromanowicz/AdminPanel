package ex.rr.adminpanel.scheduler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Data
@Component
public class TaskRunner implements Runnable{

    private TaskDefinition taskDefinition;

    @Override
    public void run() {
        switch (taskDefinition.getActionType()){
            case LOG -> logTaskExecution();
            case TEAMS_MESSAGE -> sendTeamsMessage();
        }
    }

    private void sendTeamsMessage() {
        log.info("This will be sending a message.");
    }

    private void logTaskExecution() {
        log.info("Running scheduled task [{}], with data: [{}]", taskDefinition.getCronExpression(), taskDefinition.getData());
    }


}
