package ex.rr.adminpanel.services;

import ex.rr.adminpanel.database.Trigger;
import ex.rr.adminpanel.database.TriggerRepository;
import ex.rr.adminpanel.scheduler.TaskDefinition;
import ex.rr.adminpanel.scheduler.TaskRunner;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code TriggerService} service class for Trigger related actions.
 * <p>Active triggers are loaded and scheduled during initialization.</p>
 *
 * @author  rromanowicz
 * @see     Trigger
 */
@RequiredArgsConstructor
@Service
public class TriggerService {

    private final SchedulingService schedulingService;
    private final QueryService queryService;

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
        TaskRunner taskRunner = new TaskRunner(queryService);
        taskRunner.setTaskDefinition(
                TaskDefinition.fromTrigger(trigger)
        );
        schedulingService.scheduleTask(trigger.getId(), taskRunner, trigger.getCron());
    }
}
