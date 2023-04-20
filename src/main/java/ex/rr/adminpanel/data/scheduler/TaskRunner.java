package ex.rr.adminpanel.data.scheduler;

import ex.rr.adminpanel.data.services.QueryTupleService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * The {@code TaskRunner} class represents runner for {@code TaskDefinition}.
 * {@code TaskDefinition.data} is executed based on {@code TaskDefinition.inputType}
 * and triggers action defined in {@code TaskDefinition.actionType} if execution is successful.
 *
 * @author rromanowicz
 * @see TaskDefinition
 */
@Slf4j
@Data
@Component
public class TaskRunner implements Runnable {

    private ApplicationContext applicationContext;
    private QueryTupleService queryTupleService;

    private TaskDefinition taskDefinition;

    public TaskRunner() {
    }

    public TaskRunner(QueryTupleService queryTupleService) {
        this();
        this.queryTupleService = queryTupleService;
    }

    @Override
    public void run() {
        String output = switch (taskDefinition.getInputType()) {
            case QUERY -> queryTupleService.withQuery(taskDefinition.getData()).toJson();
            case CURL -> null;
            case TEXT -> taskDefinition.getData();
        };

        if (output == null || output.isEmpty()) {
            logEmptyResults();
        } else {
            switch (taskDefinition.getActionType()) {
                case LOG -> logTaskExecution(output);
                case TEAMS_MESSAGE -> sendTeamsMessage(output);
            }
        }
    }

    private void sendTeamsMessage(String data) {
        log.info(">>TEAMS_MESSAGE<< {}", data);
    }

    private void logTaskExecution(String data) {
        log.info("Scheduled task executed. [{}]", data);
    }

    private void logEmptyResults() {
        log.info("Scheduled task [{}:{}] executed successfully but didn't return any data.",
                taskDefinition.getInputType().toString(),
                taskDefinition.getId());
    }

}
