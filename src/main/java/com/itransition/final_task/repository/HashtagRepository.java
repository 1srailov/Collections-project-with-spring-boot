package com.itransition.final_task.repository;

import com.itransition.final_task.models.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    Boolean existsByName(String name);

}