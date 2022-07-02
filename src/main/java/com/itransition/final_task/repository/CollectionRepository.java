package com.itransition.final_task.repository;

import com.itransition.final_task.models.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByOrderByIdDesc(Pageable pageable);

    @Query("SELECT a.userId from Collection a where a.id = :id")
    Long getUserIdById(Long id);


//    @Query(value = "select a.userId," +
//            "a.name, a.description, a.id, a.imageAddress, a.topicId" +
//            " from Collection a where a.id in (select b.collection_id from Item b" +
//            "group by b.collection_id order by count(b.collection_id)desc limit 5)")
//    List<Collection> getTopCollections();

}