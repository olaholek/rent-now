package pl.holowinska.rentnowbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.holowinska.rentnowbackend.jobs.JobContext;
import pl.holowinska.rentnowbackend.jobs.UpdateBookingStatusJob;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    private UpdateBookingStatusJob updateBookingStatusJob;

    @Autowired
    public SchedulerConfig(UpdateBookingStatusJob updateBookingStatusJob) {
        this.updateBookingStatusJob = updateBookingStatusJob;
    }

    //    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(fixedDelay = 5000)
    public void executeUpdateStatusJob() {
        updateBookingStatusJob.executeJob(new JobContext());
    }
}
