package ex.rr.adminpanel.data.services;

import ex.rr.adminpanel.ui.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

/**
 * The {@code SchedulingService} service class for Trigger execution related actions.
 *
 * @author rromanowicz
 * @see TaskScheduler
 */
@Slf4j
@Service
public class SchedulingService {

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    @PostConstruct
    private void init() {

    }

    /**
     * Schedule an existing task for execution.
     *
     * @param jobId          id of existing task
     * @param tasklet        Runnable with task definition
     * @param cronExpression Cron expression to schedule the task
     */
    public void scheduleTask(String jobId, Runnable tasklet, String cronExpression) {
        removeTask(jobId);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
        log.info("User: [{}] scheduled task [[{}] [{}]]",
                getUser(),
                jobId, cronExpression);
    }

    /**
     * Disable active, scheduled task.
     *
     * @param jobId id of the scheduled task
     */
    public void removeTask(String jobId) {
        ScheduledFuture<?> scheduledFuture = jobsMap.get(jobId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            jobsMap.put(jobId, null);
            log.info("User: [{}] disabled task [{}]",
                    getUser(),
                    jobId);
        }
    }

    /**
     * Returns username of currently logged user.
     *
     * @return username
     */
    private static String getUser() {
        if (Utils.getUserSession() != null) {
            return StringUtils.capitalize(Utils.getUserSession().getUsername());
        } else {
            return "Root";
        }
    }
}
