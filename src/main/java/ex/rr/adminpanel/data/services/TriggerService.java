package ex.rr.adminpanel.data.services;

import ex.rr.adminpanel.data.database.Trigger;
import ex.rr.adminpanel.data.database.TriggerRepository;
import ex.rr.adminpanel.data.scheduler.TaskDefinition;
import ex.rr.adminpanel.data.scheduler.TaskRunner;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    private final SchedulingService schedulingService;
    private final QueryTupleService queryTupleService;

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
        TaskRunner taskRunner = new TaskRunner(queryTupleService);
        taskRunner.setTaskDefinition(
                TaskDefinition.fromTrigger(trigger)
        );
        schedulingService.scheduleTask(trigger.getId(), taskRunner, trigger.getCron());
    }
}
