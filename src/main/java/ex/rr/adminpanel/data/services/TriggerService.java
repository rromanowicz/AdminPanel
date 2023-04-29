package ex.rr.adminpanel.data.services;

import ex.rr.adminpanel.data.database.Trigger;
import ex.rr.adminpanel.data.database.TriggerRepository;
import ex.rr.adminpanel.data.enums.Env;
import ex.rr.adminpanel.data.scheduler.TaskDefinition;
import ex.rr.adminpanel.data.scheduler.TaskRunner;
import ex.rr.adminpanel.ui.Session;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

/**
 * The {@code TriggerService} service class for Trigger related actions.
 * <p>Active triggers are loaded and scheduled during initialization.</p>
 *
 * @author rromanowicz
 * @see Trigger
 */
@RequiredArgsConstructor
@Service
public class TriggerService {

    private final ApplicationContext context;
    private final SchedulingService schedulingService;
    private final QueryResultSetService queryResultSetService;

    private final TriggerRepository triggerRepository;

    @PostConstruct
    private void init() {
        finActive().forEach(this::scheduleTask);
    }

    public List<Trigger> findAll() {
        return triggerRepository.findAll();
    }

    private List<Trigger> finActive() {
        return triggerRepository.findAllByEnabled(true);
    }

    public void save(Trigger trigger) {
        Trigger entity = triggerRepository.save(trigger);
        if (entity.getEnabled()) {
            scheduleTask(trigger);
        } else {
            schedulingService.removeTask(entity.getId());
        }
    }

    public void delete(String id) {
        triggerRepository.deleteById(id);
    }

    private void scheduleTask(Trigger trigger) {
        Session session = new Session(this.getClass().getName(), context);
        session.setEnv(trigger.getEnv());
        TaskRunner taskRunner = new TaskRunner(queryResultSetService, session);
        taskRunner.setTaskDefinition(
                TaskDefinition.fromTrigger(trigger)
        );
        schedulingService.scheduleTask(trigger.getId(), taskRunner, trigger.getCron());
    }

    public void disableTask(String id) {
        Optional<Trigger> trigger = triggerRepository.findById(id);
        if (trigger.isPresent()) {
            trigger.get().setEnabled(false);
            save(trigger.get());
        }
    }


    private DataSource getEnvDataSource(Env env) {
        return switch (env) {
            case LOCAL -> context.getBean("dsLocal", DataSource.class);
            case DEV -> context.getBean("dsDev", DataSource.class);
            case SIT -> context.getBean("dsSit", DataSource.class);
            case SAT -> context.getBean("dsSat", DataSource.class);
            case PROD -> context.getBean("dsProd", DataSource.class);
        };
    }
}
