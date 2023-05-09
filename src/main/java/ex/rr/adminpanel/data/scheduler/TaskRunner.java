package ex.rr.adminpanel.data.scheduler;

import java.net.URISyntaxException;

import ex.rr.adminpanel.data.services.QueryResultSetService;
import ex.rr.adminpanel.data.services.RestService;
import ex.rr.adminpanel.ui.Session;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * The {@code TaskRunner} class represents runner for {@code TaskDefinition}. {@code
 * TaskDefinition.data} is executed based on {@code TaskDefinition.inputType} and triggers action
 * defined in {@code TaskDefinition.actionType} if execution is successful.
 *
 * @author rromanowicz
 * @see TaskDefinition
 */
@Slf4j
@Data
public class TaskRunner implements Runnable {

  private QueryResultSetService queryResultSetService;
  private RestService restService;
  private Session session;

  private TaskDefinition taskDefinition;

  private TaskRunner() {}

  public TaskRunner(
      QueryResultSetService queryResultSetService, RestService restService, Session session) {
    this();
    this.queryResultSetService = queryResultSetService;
    this.restService = restService;
    this.session = session;
  }

  @Override
  public void run() {
    try {
      if (taskResults()) {
        logTaskExecution(true);
        switch (taskDefinition.getActionType()) {
          case LOG -> log.info(taskDefinition.getData());
          case TEAMS_MESSAGE -> sendTeamsMessage();
          case REST -> restService.get(taskDefinition.getData());
        }
      } else {
        logTaskExecution(false);
      }
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean taskResults() throws URISyntaxException {
    return switch (taskDefinition.getInputType()) {
      case QUERY -> queryResultSetService.query(session.getDataSource(), taskDefinition.getData());
      case CURL, POST -> false;
      case GET -> hasData(restService.get(taskDefinition.getData()));
      case TEXT -> !taskDefinition.getData().isBlank();
    };
  }

  private boolean hasData(Mono<String> mono) {
    return mono.block().length() > 2;
  }

  private void sendTeamsMessage() {
    String url = "";
    try {
      restService.postTeamsMessage(
          url, String.format("[%s:%s]", taskDefinition.getEnv(), taskDefinition.getName()));
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private void logTaskExecution(boolean returnedData) {
    log.info(
        ">>TEAMS_MESSAGE<< Scheduled task [{}:{}] executed successfully and returned data.",
        taskDefinition.getEnv(),
        taskDefinition.getName(),
        (returnedData) ? "and returned" : "but didn't return any");
  }
}
