package pl.holowinska.rentnowbackend.jobs;

public interface Job {
    JobStatus executeJob(JobContext jobContext);
}
