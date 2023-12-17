package com.example.Loyalty.repositories;

import com.example.Loyalty.models.Event;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAll() ;
    Optional<Event> findById(Long id) ;

    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate and :endDate")
    List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    // List<Event> findByDescription
    Event save(Event event);
    void delete(Event event);
    void deleteById(Long id) ;

    @Query("SELECT e FROM Event e JOIN e.members m WHERE m.id = :memberId")
    List<Event> getEventsByMemberId(Long memberId);

    @Query("SELECT m FROM Member m JOIN m.events e WHERE e.id = :eventId")
    List<Member> getMembersByEventId(Long eventId);

    @Query("SELECT r FROM Reward r JOIN r.events e WHERE e.id= :eventId")
    List<Reward> getRewardsByEventId(Long eventId);



}
