package com.itransition.final_task.repository;

import com.itransition.final_task.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {


    Optional<Like> findByUserIdAndItemId(Long userId, Long itemId);

    void deleteLikeByUserIdAndItemId(Long userId, Long itemId);
}