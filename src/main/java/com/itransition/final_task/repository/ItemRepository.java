package com.itransition.final_task.repository;

import com.itransition.final_task.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>{
}