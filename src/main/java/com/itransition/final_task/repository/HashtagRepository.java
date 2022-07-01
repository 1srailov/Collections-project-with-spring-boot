package com.itransition.final_task.repository;

import com.itransition.final_task.models.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Boolean existsByName(String name);

}