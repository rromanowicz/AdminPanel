package ex.rr.adminpanel.datasource.services;

import ex.rr.adminpanel.datasource.database.Trigger;
import ex.rr.adminpanel.datasource.database.repository.TriggerRepository;
import ex.rr.adminpanel.datasource.scheduler.TaskDefinition;
import ex.rr.adminpanel.datasource.scheduler.TaskRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TriggerService {

    private final SchedulingService schedulingService;
    private final QueryService queryService;

    private final TriggerRepository triggerRepository;

    public List<Trigger> findAll() {
        return triggerRepository.findAll();
    }

    public void save(Trigger trigger) {
        Trigger entity = triggerRepository.save(trigger);
        if (entity.getEnabled()) {
            TaskRunner taskRunner = new TaskRunner(queryService);
            taskRunner.setTaskDefinition(
                    TaskDefinition.fromTrigger(trigger)
            );
            schedulingService.scheduleTask(entity.getId(), taskRunner, entity.getCron());
        } else {
            schedulingService.removeTask(entity.getId());
        }
    }

    public void delete(String id) {
        triggerRepository.deleteById(id);
    }
}
