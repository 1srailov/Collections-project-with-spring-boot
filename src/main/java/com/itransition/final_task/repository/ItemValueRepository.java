package com.itransition.final_task.repository;

import com.itransition.final_task.models.ItemValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemValueRepository extends JpaRepository<ItemValue, Long> {
}