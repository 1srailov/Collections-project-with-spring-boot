package com.itransition.final_task.repository;

import com.itransition.final_task.models.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByOrderByIdDesc(Pageable pageable);

    boolean existsByUserId(Long userId);

    @Query("SELECT a.userId from Collection a where a.id = :id")
    Long getUserIdById(Long id);

//    @Query("select a from Collection a where a.id in " +
//            "(select b.collectionId from Item b group by b.collectionId order by count(b.collectionId)desc limit 5)")
//    List<Collection> getTopCollections();

        @Query("select a from Collection a " +
                "left join Item b on a.id = b.collectionId " +
                "group by b.collectionId")
    List<Collection> getTopCollections(Pageable pageable);
        
}