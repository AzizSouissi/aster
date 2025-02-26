package com.batchie.config;

import com.batchie.domain.Shipment;
import com.batchie.domain.ShipmentResult;
import com.batchie.listener.ShipmentJobCompletionListener;
import com.batchie.processor.ShipmentProcessor;
import com.batchie.reader.ShipmentReader;
import com.batchie.writer.ShipmentWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class ShipmentBatchConfig {

    @Autowired
    private ShipmentReader shipmentReader;

    @Autowired
    private ShipmentProcessor shipmentProcessor;

    @Autowired
    private ShipmentWriter shipmentWriter;

    @Autowired
    private ShipmentJobCompletionListener jobCompletionListener;

    @Bean
    public Job processShipmentsJob(JobRepository jobRepository, Step processShipmentsStep) {
        return new JobBuilder("processShipmentsJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .flow(processShipmentsStep)
                .end()
                .build();
    }

    @Bean
    public Step processShipmentsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processShipmentsStep", jobRepository)
                .<Shipment, ShipmentResult>chunk(10, transactionManager)
                .reader(shipmentReader)
                .processor(shipmentProcessor)
                .writer(shipmentWriter)
                .build();
    }
}