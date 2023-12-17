package com.example.Loyalty.services.implementations;

import com.example.Loyalty.dtos.EventDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.mappers.EventMapper;
import com.example.Loyalty.mappers.MemberMapperImpl;
import com.example.Loyalty.mappers.RewardMapperImpl;
import com.example.Loyalty.models.*;
import com.example.Loyalty.repositories.CampaignRepository;
import com.example.Loyalty.repositories.EventRepository;
import com.example.Loyalty.repositories.MemberRepository;
import com.example.Loyalty.repositories.RewardRepository;
import com.example.Loyalty.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    private EventMapper eventMapper;
    private RewardRepository rewardRepository;
    private CampaignRepository campaignRepository;
    private MemberMapperImpl memberMapper;
    private RewardMapperImpl rewardMapper;
    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, RewardRepository rewardRepository, CampaignRepository campaignRepository, MemberMapperImpl memberMapper, RewardMapperImpl rewardMapper){
        this.eventRepository= eventRepository;
        this.rewardRepository= rewardRepository;
        this.eventMapper= eventMapper;
        this.campaignRepository= campaignRepository;
        this.memberMapper= memberMapper;
        this.rewardMapper= rewardMapper;
    }
    @Override
    public EventDTO createEvent(EventDTO eventData) {
        Event event= eventMapper.convertToEvent(eventData);
        event= eventRepository.save(event);

        return eventMapper.convertToEventDTO(event);
    }

    @Override
    public EventDTO updateEvent(Long eventId, EventDTO eventData) {
        Event isEventExist = eventRepository.findById(eventId)
                .orElseThrow(()-> new NoSuchElementException("Event not found with ID: " + eventId));
        isEventExist.setEventName(eventData.getEventName());
        isEventExist.setEventDate(eventData.getEventDate());
        isEventExist.setDescription(eventData.getDescription());
        isEventExist= eventRepository.save(isEventExist);
        return eventMapper.convertToEventDTO(isEventExist);
    }

    @Override
    public boolean deleteEvent(Long eventId) {
        try {
            eventRepository.deleteById(eventId);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Event event= eventRepository.findById(eventId).orElse(null);
        if(event == null){
            return null;
        }
        else{
            return eventMapper.convertToEventDTO(event);
        }
    }

    @Override
    public List<EventDTO> getAllEvents() {
        try{
            List<Event> events= eventRepository.findAll();
            List<EventDTO> eventDTOS = new ArrayList<>();
            for(Event event: events){
                eventDTOS.add(eventMapper.convertToEventDTO(event));
            }
            return  eventDTOS;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<EventDTO> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        try{
            List<Event> events= eventRepository.getEventsByDateRange(startDate, endDate);
            List<EventDTO> eventDTOS= new ArrayList<>();
            for(Event event: events){
                eventDTOS.add(eventMapper.convertToEventDTO(event));
            }
            return eventDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<EventDTO> getEventsByDescription(String keyword) {
        try{
            List<Event> events= eventRepository.findAll();
            List<EventDTO> eventDTOS= new ArrayList<>();
            keyword=keyword.toLowerCase();
            for(Event event: events){
                if ((event.getDescription()!=null && event.getDescription().toLowerCase().contains(keyword))
                        ||(event.getEventName()!=null && event.getEventName().toLowerCase().contains(keyword))
                        ||( event.getCampaigns()!=null && event.getCampaigns().contains(keyword))){
                    eventDTOS.add(eventMapper.convertToEventDTO(event));
                }
            }
            return eventDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void participateInEvent(Long eventId, Long memberId) {

    }

    @Override
    public List<EventDTO> getEventsForMember(Long memberId) {
        try{
            List<Event> events= eventRepository.getEventsByMemberId(memberId);
            List<EventDTO> eventDTOS= new ArrayList<>();
            for(Event event: events){
                eventDTOS.add(eventMapper.convertToEventDTO(event));
            }
            return eventDTOS;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }


    @Override
    public List<RewardDTO> getRewardsForEvent(Long eventId) {
        try{
            List<Reward> rewards= eventRepository.getRewardsByEventId(eventId);
            List<RewardDTO> rewardDTOS= new ArrayList<>();
            for (Reward reward: rewards){
                rewardDTOS.add(rewardMapper.rewardmodelMapper().map(reward, RewardDTO.class));
            }
            return rewardDTOS;
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public List<MemberDTO> getParticipantsForEvent(Long eventId) {
        try{
            List<Member> members= eventRepository.getMembersByEventId(eventId);
            List<MemberDTO> memberDTOS= new ArrayList<>();
            for(Member member: members){
                memberDTOS.add(memberMapper.fromMember(member));
            }
            return memberDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }


}
