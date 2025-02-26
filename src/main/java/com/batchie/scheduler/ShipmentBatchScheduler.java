package com.batchie.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.batchie.service.ProviderRotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class ShipmentBatchScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentBatchScheduler.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job processShipmentsJob;

    @Autowired
    private ProviderRotationService providerRotationService;

    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // Run every 6 hours
    public void scheduleShipmentProcessing() {
        String currentProvider = providerRotationService.getNextProvider();
        logger.info("Starting batch processing for provider: {}", currentProvider);

        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("time", new Date())
                    .addString("provider", currentProvider)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(processShipmentsJob, jobParameters);
            logger.info("Job execution status: {}", jobExecution.getStatus());

        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            logger.error("Error occurred while starting the job", e);
        }
    }
}
