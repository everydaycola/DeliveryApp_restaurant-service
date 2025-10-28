package be.kdg.sa.restaurantservice.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@EnableScheduling
@Slf4j
public class PublishDishesScheduler {

    private final TaskScheduler taskScheduler;

    public PublishDishesScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void scheduleTask(Runnable task, Date runTime) {
        Instant instant = runTime.toInstant();
        log.info("Scheduling task to run at: {}", runTime);
        taskScheduler.schedule(task, instant);
    }
}
