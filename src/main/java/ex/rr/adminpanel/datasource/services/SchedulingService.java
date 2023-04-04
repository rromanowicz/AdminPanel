package ex.rr.adminpanel.datasource.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulingService {

    @Autowired
    private TaskScheduler taskScheduler;

    private Map<String, ScheduledFuture<?>> jobsMap = new HashMap<>();

    public void scheduleTask(String jobId, Runnable tasklet, String cronExpression) {
        removeTask(jobId);
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(tasklet, new CronTrigger(cronExpression, TimeZone.getTimeZone(TimeZone.getDefault().getID())));
        jobsMap.put(jobId, scheduledTask);
        log.info("User: [{}] scheduled task [[{}] [{}]]",
                StringUtils.capitalize(SecurityContextHolder.getContext().getAuthentication().getName()),
                jobId, cronExpression);
    }

    public void removeTask(String jobId) {
        ScheduledFuture<?> scheduledFuture = jobsMap.get(jobId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            jobsMap.put(jobId, null);
            log.info("User: [{}] disabled task [{}]",
                    StringUtils.capitalize(SecurityContextHolder.getContext().getAuthentication().getName()),
                    jobId);
        }
    }
}
