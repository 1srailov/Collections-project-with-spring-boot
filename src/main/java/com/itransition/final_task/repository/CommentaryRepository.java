package com.itransition.final_task.repository;

import com.itransition.final_task.models.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
}