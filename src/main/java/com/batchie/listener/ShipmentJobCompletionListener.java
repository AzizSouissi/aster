package com.batchie.listener;

import com.batchie.reader.ShipmentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipmentJobCompletionListener implements JobExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(ShipmentJobCompletionListener.class);

    @Autowired
    private ShipmentReader shipmentReader;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job started: {}", jobExecution.getJobInstance().getJobName());

        // Initialize the reader with the current provider
        String provider = jobExecution.getJobParameters().getString("provider");
        shipmentReader.initialize(provider);

        logger.info("Reader initialized for provider: {}", provider);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("Job completed successfully: {}", jobExecution.getJobInstance().getJobName());
        } else {
            logger.warn("Job failed: {} with status: {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
        }

        logger.info("Read count: {}", jobExecution.getStepExecutions().stream()
                .mapToInt(step -> Math.toIntExact(step.getReadCount())).sum());
        logger.info("Write count: {}", jobExecution.getStepExecutions().stream()
                .mapToInt(step -> Math.toIntExact(step.getWriteCount())).sum());
        logger.info("Skip count: {}", jobExecution.getStepExecutions().stream()
                .mapToInt(step -> Math.toIntExact(step.getSkipCount())).sum());
    }
}