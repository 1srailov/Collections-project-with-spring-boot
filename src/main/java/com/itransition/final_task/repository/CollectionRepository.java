package com.itransition.final_task.repository;

import com.itransition.final_task.models.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findByOrderByIdDesc(Pageable pageable);

    List<Collection> findAllByUserId(Long userId);

    boolean existsByUserId(Long userId);

    @Query("SELECT a.userId from Collection a where a.id = :id")
    Long getUserIdById(Long id);

    @Query("select a from Collection a where a.id in " +
            "(select b.collectionId from Item b group by b.collectionId order by count(b.collectionId)desc)")
    List<Collection> getTopCollections(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "CALL delete_collection(:collection_id); commit", nativeQuery = true)
    void deleteCollectionById(@Param("collection_id") Long collectionId);

}