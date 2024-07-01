package com.jesussb.documentservice.commons.config;

import com.jesussb.documentservice.document.batch.processors.DocumentProcessor;
import com.jesussb.documentservice.document.batch.readers.RestDocumentReader;
import com.jesussb.documentservice.document.batch.writer.DocumentWriter;
import com.jesussb.documentservice.document.dto.MailXmlDocument;
import com.jesussb.documentservice.mailsupplier.MailSupplierClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final DocumentWriter documentWriter;
    private final MailSupplierClient mailSupplierClient;

    @Bean(name = "processDocumentsJob")
    public Job processDocumentsJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, Step step) {
        return new JobBuilder("processDocumentsJob", jobRepository)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step processDocumentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processDocumentStep", jobRepository)
                .<String, MailXmlDocument>chunk(20, transactionManager)
                .reader(restDocumentReader())
                .processor(documentProcessor())
                .writer(documentWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<String> restDocumentReader() {
        return new RestDocumentReader(mailSupplierClient);
    }

    @Bean
    @StepScope
    public ItemProcessor<String, MailXmlDocument> documentProcessor() {
        return new DocumentProcessor();
    }

}
