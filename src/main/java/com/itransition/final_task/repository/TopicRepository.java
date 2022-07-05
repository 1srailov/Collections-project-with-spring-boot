package com.itransition.final_task.repository;

import com.itransition.final_task.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Query("SELECT a.name from Topic a where a.id = :id")
    String getTopicNameById(Integer id);
}