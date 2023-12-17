package com.example.Loyalty.repositories;


import com.example.Loyalty.enums.ActionType;
import com.example.Loyalty.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByMemberId(Long id);

    //List<Action> findActionsByType(ActionType actionType);

    @Query("SELECT a FROM Action a WHERE a.date BETWEEN :startDate AND :endDate")
    List<Action> getActionByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Action> findActionById(Long id);
    @Query("SELECT a FROM Action a WHERE a.type = :actionType")
    List<Action> findActionsByTypeIgnoreCase(ActionType actionType);
}