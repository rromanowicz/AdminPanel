package ex.rr.adminpanel.data.scheduler;

import ex.rr.adminpanel.data.services.QueryResultSetService;
import ex.rr.adminpanel.ui.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    private QueryResultSetService queryResultSetService;
    private Session session;


    private TaskDefinition taskDefinition;

    private TaskRunner() {
    }

    public TaskRunner(QueryResultSetService queryResultSetService, Session session) {
        this();
        this.queryResultSetService = queryResultSetService;
        this.session = session;
    }

    @Override
    public void run() {
        String output = switch (taskDefinition.getInputType()) {
            case QUERY -> queryResultSetService.query(session.dataSource(), taskDefinition.getData()).toString();
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
