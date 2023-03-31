package ex.rr.adminpanel.services;

import ex.rr.adminpanel.database.DbTrigger;
import ex.rr.adminpanel.database.DbTriggerRepository;
import ex.rr.adminpanel.scheduler.TaskDefinition;
import ex.rr.adminpanel.scheduler.TaskRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TriggerService {

    private final SchedulingService schedulingService;

    private final DbTriggerRepository dbTriggerRepository;

    public List<DbTrigger> getDbTriggers() {
        return dbTriggerRepository.findAll();
    }

    public void save(DbTrigger dbTrigger) {
        DbTrigger entity = dbTriggerRepository.save(dbTrigger);
        if (entity.getEnabled()) {
            TaskRunner taskRunner = new TaskRunner();
            taskRunner.setTaskDefinition(
                    TaskDefinition.builder()
                            .cronExpression(entity.getCron())
                            .actionType(entity.getType())
                            .data(entity.getQuery())
                            .build()
            );
            schedulingService.scheduleTask(entity.getId().toString(), taskRunner, entity.getCron());
        } else {
            schedulingService.removeTask(entity.getId().toString());
        }
    }

}
