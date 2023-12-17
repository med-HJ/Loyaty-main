package com.example.Loyalty.services;


import com.example.Loyalty.models.Event;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
public interface EventService {
    Event createEvent(Event eventData);
    Event updateEvent(Long eventId, Event eventData);
    boolean deleteEvent(Long eventId);
    Event getEventById(Long eventId);
    List<Event> getAllEvents();
    List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> getEventsByDescription(String keyword);
    void participateInEvent(Long eventId, Long memberId);
    List<Event> getEventsForMember(Long memberId);
    List<Reward> getRewardsForEvent(Long eventId);
    void associateRewardsWithEvent(Long eventId, List<Long> rewardsId);
    void addCampaignsToEvent(Long eventId, List<Long> campaignIds);
    boolean removeCampaignsFromEvent(Long eventId, List<Long> campaignIds);
    boolean removeRewardFromEvent(Long eventId, List<Long> rewardIds);
    List<Member> getParticipantsForEvent(Long eventId);
}
