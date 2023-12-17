package com.example.Loyalty.services;

import com.example.Loyalty.dtos.EventDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.models.Event;
import com.example.Loyalty.models.Member;
import com.example.Loyalty.models.Reward;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Service
public interface EventService {
    EventDTO createEvent(EventDTO eventData);
    EventDTO updateEvent(Long eventId, EventDTO eventData);
    boolean deleteEvent(Long eventId);
    EventDTO getEventById(Long eventId);
    List<EventDTO> getAllEvents();
    List<EventDTO> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<EventDTO> getEventsByDescription(String keyword);
    void participateInEvent(Long eventId, Long memberId);
    List<EventDTO> getEventsForMember(Long memberId);
    List<RewardDTO> getRewardsForEvent(Long eventId);
    void associateRewardsWithEvent(Long eventId, List<Long> rewardsId);
    void addCampaignsToEvent(Long eventId, List<Long> campaignIds);
    boolean removeCampaignsFromEvent(Long eventId, List<Long> campaignIds);
    boolean removeRewardFromEvent(Long eventId, List<Long> rewardIds);
    List<MemberDTO> getParticipantsForEvent(Long eventId);
}
