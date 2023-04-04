package ex.rr.adminpanel.datasource.scheduler;

import ex.rr.adminpanel.datasource.services.QueryService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Slf4j
@Data
@Component
public class TaskRunner implements Runnable {

    private ApplicationContext applicationContext;
    private QueryService queryService;

    private TaskDefinition taskDefinition;

    public TaskRunner() {
    }

    public TaskRunner(QueryService queryService) {
        this();
        this.queryService = queryService;
    }

    @Override
    public void run() {
        String output = switch (taskDefinition.getInputType()) {
            case QUERY -> queryService.withQuery(taskDefinition.getData()).toJson();
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
