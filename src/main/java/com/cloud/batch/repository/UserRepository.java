package com.cloud.batch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

import com.cloud.batch.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastLoginAtBetween(LocalDateTime startAt, LocalDateTime endAt);
    /*public JpaPagingItemReader<User> inactiveUserJpaReader(@Value("#{jobParameters[nowDate]}") Date nowDate) {
        JpaPagingItemReader<User> jpaPagingItemReader = new JpaPagingItemReader<>();
        jpaPagingItemReader.setQueryString("select u from User as u where u.createdDate < :createdDate and u.status = :status"); //(2)

        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());
        map.put("createdDate", now.minusYears(1));
        map.put("status", 1);

        jpaPagingItemReader.setParameterValues(map); //(3)
        jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory); //(4)
        jpaPagingItemReader.setPageSize(CHUNK_SIZE); //(5)
    }*/

}
