package com.cloud.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import lombok.RequiredArgsConstructor;
import com.cloud.batch.model.User;

@Configuration
@RequiredArgsConstructor
public class InactiveUserJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final String JOB_NAME = "deactivateUserJob";
    private static final int CHUNK_SIZE = 10;

    /*
    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory, Step inactiveJobStep) {
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .start(inactiveJobStep)
                .build();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("inactiveJobStep")
                .<User, User>chunk(10)
                .reader(inactiveUserReader())
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }

    @Bean
    @StepScope
    public QueueItemReader<User> inactiveUserReader(StepBuilderFactory stepBuilderFactory) {
        List<User> oldUsers = userRepository.findByUpdatedDateBeforeAndStatusEquals(
                    LocalDateTime.now().minusYears(1),
                    UserStatus.ACTIVE);

        return new QueueItemReader<>(oldUsers); //(3)
    }


    public ItemProcessor<User, User> inactiveUserProcessor() {
        return user -> user.setInactive();
    }

    public ItemWriter<User> inactiveUserWriter() {
        return ((List<? extends User> users) -> userRepository.saveAll(users));
    }*/

    @Bean
    public Job job(){
        return jobBuilderFactory.get(JOB_NAME)
                //.preventRestart()
                .start(step(null))
                .build();
    }

    @Bean
    @JobScope
    public Step step(@Value("#{jobParameters[requestDate]}") String requetDate){
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .<User, User>chunk(CHUNK_SIZE)
                .reader(deactivateUserReader())
                .processor(deactivateUserProcessor())
                .writer(deactivateUserWriter())
                .build();

    }


    @Bean
    @StepScope
    public JpaPagingItemReader<User> deactivateUserReader(){
        return new JpaPagingItemReaderBuilder<User>()
                .name(JOB_NAME + "Reader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT u FROM User u where u.active = 1")
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @Bean
    public ItemProcessor<User, User> deactivateUserProcessor() {
        return user -> {
            user.setActive(false);
            return user;
        };
    }

    private ItemWriter<User> deactivateUserWriter() {
        return users -> users.stream().forEach(user -> System.out.println(user.getName() + " = " + user.getActive()));
    }
}
