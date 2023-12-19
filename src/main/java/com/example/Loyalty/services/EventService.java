package com.example.Loyalty.services;



import com.example.Loyalty.models.*;
import com.example.Loyalty.repositories.CampaignRepository;
import com.example.Loyalty.repositories.EventRepository;
import com.example.Loyalty.repositories.RewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    private RewardRepository rewardRepository;
    private CampaignRepository campaignRepository;

    @Autowired
    public EventService(EventRepository eventRepository, RewardRepository rewardRepository, CampaignRepository campaignRepository){
        this.eventRepository= eventRepository;
        this.rewardRepository= rewardRepository;
        this.campaignRepository= campaignRepository;

    }

    public Event createEvent(Event eventData) {
        Event event = eventRepository.save(eventData);
        return event;
    }


    public Event updateEvent(Long eventId, Event eventData) {
        Event isEventExist = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + eventId));

        isEventExist.setEventName(eventData.getEventName());
        isEventExist.setEventDate(eventData.getEventDate());
        isEventExist.setDescription(eventData.getDescription());

        Event updatedEvent = eventRepository.save(isEventExist);
        return updatedEvent;
    }


    public boolean deleteEvent(Long eventId) {
        try {
            eventRepository.deleteById(eventId);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }


    public List<Event> getAllEvents() {
        try {
            return eventRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            return eventRepository.getEventsByDateRange(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public List<Event> getEventsByDescription(String keyword) {
        try {
            List<Event> events = eventRepository.findAll();
            List<Event> filteredEvents = new ArrayList<>();
            keyword = keyword.toLowerCase();

            for (Event event : events) {
                if ((event.getDescription() != null && event.getDescription().toLowerCase().contains(keyword))
                        || (event.getEventName() != null && event.getEventName().toLowerCase().contains(keyword))) {
                    filteredEvents.add(event);
                }
            }
            return filteredEvents;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public void participateInEvent(Long eventId, Long memberId) {

    }


    public List<Event> getEventsForMember(Long memberId) {
        try {
            List<Event> events = eventRepository.getEventsByMemberId(memberId);
            return events;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public List<Reward> getRewardsForEvent(Long eventId) {
        try {
            List<Reward> rewards = eventRepository.getRewardsByEventId(eventId);
            return rewards;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



    public void associateRewardsWithEvent(Long eventId, List<Long> rewardsId) {
        Event event= eventRepository.findById(eventId)
                .orElseThrow(()-> new NoSuchElementException("Event not found with ID: " + eventId));

        List<Reward> rewards= rewardRepository.findAllById(rewardsId);

        if(rewards.isEmpty()){
            throw new NoSuchElementException("No rewards found with the provided IDs");
        }
        List<Reward> eventRewards = event.getRewards();
        for(Reward reward: rewards){
            if(!eventRewards.contains(reward)){
                eventRewards.add(reward);
            }
        }
        eventRepository.save(event);
    }


    public void addCampaignsToEvent(Long eventId, List<Long> campaignIds) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new NoSuchElementException("Event not found with ID: " + eventId));

        List<Campaign> campaigns= campaignRepository.findAllById(campaignIds);

        if(campaigns.isEmpty()){
            throw  new NoSuchElementException("No campaigns found with the provided IDs");
        }

        List<Campaign> eventCampaign = event.getCampaigns();

        for(Campaign campaign: campaigns){
            if(!eventCampaign.contains(campaign)){
                eventCampaign.add(campaign);
            }
        }

        eventRepository.save(event);

    }


    public boolean removeCampaignsFromEvent(Long eventId, List<Long> campaignIds) {
        Event event= eventRepository.findById(eventId)
                .orElseThrow(()-> new NoSuchElementException("Event not found with ID: " + eventId));

        List<Campaign> campaigns = campaignRepository.findAllById(campaignIds);
        if (campaigns.isEmpty()){
            throw  new NoSuchElementException("No campaigns found with the provided IDs");
        }
        List<Campaign> eventCampaigns = event.getCampaigns();
        int initialSize = eventCampaigns.size();
        eventCampaigns.removeIf(campaign -> campaignIds.contains(campaign.getId()));
        eventCampaigns.removeAll(campaigns);
        if (eventCampaigns.size() < initialSize) {
            eventRepository.save(event);
            return true;
        } else {
            return false;
        }
    }


    public boolean removeRewardFromEvent(Long eventId, List<Long> rewardIds) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event not found with ID: " + eventId));

        List<Reward> rewards = rewardRepository.findAllById(rewardIds);
        if (rewards.isEmpty()) {
            throw new NoSuchElementException("No rewards found with the provided IDs");
        }

        List<Reward> eventRewards = event.getRewards();
        int initialSize = eventRewards.size();

        eventRewards.removeIf(reward -> rewardIds.contains(reward.getId()));
        if (eventRewards.size() < initialSize) {
            eventRepository.save(event);
            return true;
        } else {
            return false;
        }
    }


    public List<Member> getParticipantsForEvent(Long eventId) {
        try {
            return eventRepository.getMembersByEventId(eventId);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }



}
