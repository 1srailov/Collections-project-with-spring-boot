package com.itransition.final_task.repository;

import com.itransition.final_task.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User getUserByUsername(String username);

    @Query("SELECT a.username from User a where a.id = :id")
    String getUsernameById(Long id);

    @Query("SELECT a.id from User a where a.username = :username")
    Long getIdByUsername(String username);
}
