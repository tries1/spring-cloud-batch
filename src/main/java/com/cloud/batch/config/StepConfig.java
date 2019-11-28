package com.cloud.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StepConfig {

    private static final String STEP_NAME = "batch-step";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /*@Bean
    public Step step(){
        return stepBuilderFactory.get(STEP_NAME)
                .chunk(1)
                .reader()
    }*/

    @Bean
    public Job stepNextJob(){
        return jobBuilderFactory.get("stepNextJob")//jobBuilderFactory의 get메소드는 새로운 jobBuilder를 반환한다.(파라미터는 이름이다)
                .start(step1(null))
                .next(step2())
                .next(step3())
                .build();
    }

    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters[param1]}") String param1){
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step1. Prosessing.. {}", param1);
                    return RepeatStatus.CONTINUABLE;
                })
                .build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step2. Prosessing..");
                    return RepeatStatus.CONTINUABLE;
                })
                .build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step3. Prosessing..");
                    return RepeatStatus.CONTINUABLE;
                })
                .build();
    }
}
