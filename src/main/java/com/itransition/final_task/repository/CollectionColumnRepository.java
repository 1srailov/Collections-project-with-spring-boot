package com.itransition.final_task.repository;

import com.itransition.final_task.models.CollectionColumn;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CollectionColumnRepository extends JpaRepository<CollectionColumn, Long> {
    Boolean existsByCollectionId(Long collectionId);

    List<CollectionColumn> findAllByCollectionId(Long collectionId);


    Optional<?> findByCollectionId(Long collectionId);
}