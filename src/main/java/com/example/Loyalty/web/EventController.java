package com.example.Loyalty.web;

import com.example.Loyalty.dtos.EventDTO;
import com.example.Loyalty.dtos.MemberDTO;
import com.example.Loyalty.dtos.RewardDTO;
import com.example.Loyalty.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/loyalty/events")

public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            EventDTO createEvent = eventService.createEvent(eventDTO);
            return new ResponseEntity<>(createEvent, HttpStatus.CREATED);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Error creating event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public List<EventDTO> getAllEvents(){
        return eventService.getAllEvents();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id){
        if(id == null){
            return new ResponseEntity<>("Id is Required", HttpStatus.BAD_REQUEST);
        }
        EventDTO eventDTO= eventService.getEventById(id);
        if(eventDTO== null){
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(eventDTO, HttpStatus.OK);
        }

    }
    @PutMapping("/{id}")
    public ResponseEntity<?>updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO){
        try{
            EventDTO updateEvent= eventService.updateEvent(id, eventDTO);
            return new ResponseEntity<>(updateEvent, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>("Event Not found", HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id){
        if(id== null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        if(eventService.deleteEvent(id)){
            return  new ResponseEntity<>("Event deleted successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed to delete event", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getEventsByMemberId(@PathVariable Long id){
        if(id==null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        List<EventDTO> eventDTOS= eventService.getEventsForMember(id);
        if(eventDTOS.isEmpty()){
            return new ResponseEntity<>("No events found for this member", HttpStatus.NOT_FOUND);

        }
        else{
            return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
        }
    }
    @GetMapping("/event-description")
    public ResponseEntity<?> getEventByDescription(@RequestParam String keyword){
        if(keyword ==null || keyword.isEmpty()){
            return new ResponseEntity<>("Keyword is required", HttpStatus.BAD_REQUEST);
        }
        List<EventDTO> eventDTOS= eventService.getEventsByDescription(keyword);
        if(eventDTOS.isEmpty()){
            return new ResponseEntity<>("No events Found", HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
        }
    }
    @GetMapping("/by-date-range")
    public ResponseEntity<?> getEventByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        List<EventDTO> eventDTOS= eventService.getEventsByDateRange(startDate, endDate);
        if(eventDTOS.isEmpty()){
            return new ResponseEntity<>("No events found with this date ", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(eventDTOS, HttpStatus.OK);
        }
    }

    @PutMapping("/{eventId}/associate-rewards")
    public ResponseEntity<String> associateRewardsWithEvent(
            @PathVariable Long eventId,
            @RequestBody List<Long> rewardsId
    ){
        if (rewardsId == null || rewardsId.isEmpty()) {
            return new ResponseEntity<>("Reward IDs must be provided", HttpStatus.BAD_REQUEST);
        }

        try {
            eventService.associateRewardsWithEvent(eventId, rewardsId);
            return new ResponseEntity<>("Rewards associated with the event successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while associating rewards", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{eventId}/add-campaigns")
    public ResponseEntity<String> associateCampaignsToEvent(
            @PathVariable Long eventId,
            @RequestBody List<Long> campaignsId
    )
    {
        if (campaignsId == null || campaignsId.isEmpty()){
            return new ResponseEntity<>("Campaign IDs must be provided", HttpStatus.BAD_REQUEST);
        }
        EventDTO eventDTO= eventService.getEventById(eventId);
        if (eventDTO == null){
            return new ResponseEntity<>("No events found with this ID ", HttpStatus.NOT_FOUND);
        }
        try {
            eventService.addCampaignsToEvent(eventId, campaignsId);
            return new ResponseEntity<>("Campaigns associated with the event successfully", HttpStatus.OK);

        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while associating campaigns", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @DeleteMapping("/{eventId}/remove-campaigns")
    public ResponseEntity<String> removeCampaignsFromEvent(
            @PathVariable Long eventId,
            @RequestBody List<Long> campaignIds
    ) {
        if (campaignIds == null || campaignIds.isEmpty()) {
            return new ResponseEntity<>("Campaign IDs must be provided", HttpStatus.BAD_REQUEST);
        }

        EventDTO eventDTO= eventService.getEventById(eventId);
        if (eventDTO == null){
            return new ResponseEntity<>("No events found with this ID ", HttpStatus.NOT_FOUND);
        }
        try {
           boolean campaignsRemoved = eventService.removeCampaignsFromEvent(eventId, campaignIds);
           if(campaignsRemoved){
               return new ResponseEntity<>("Campaigns removed from the event successfully", HttpStatus.OK);
           } else {
               return new ResponseEntity<>("No campaigns associated with the event were removed", HttpStatus.OK);
           }

        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while removing campaigns", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{eventId}/remove-rewards")
    public ResponseEntity<String> removeRewardsFromEvent(
            @PathVariable Long eventId,
            @RequestBody List<Long> rewardIds
    ) {
        if (rewardIds == null || rewardIds.isEmpty()) {
            return new ResponseEntity<>("Reward IDs must be provided", HttpStatus.BAD_REQUEST);
        }

        EventDTO eventDTO = eventService.getEventById(eventId);
        if (eventDTO == null) {
            return new ResponseEntity<>("No events found with this ID", HttpStatus.NOT_FOUND);
        }

        try {
            boolean rewardsRemoved = eventService.removeRewardFromEvent(eventId, rewardIds);
            if (rewardsRemoved) {
                return new ResponseEntity<>("Rewards removed from the event successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No rewards associated with the event were removed", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while removing rewards", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<?> getParticipantsForEvent(@PathVariable Long eventId){
        if(eventId==null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        List<MemberDTO> memberDTOS= eventService.getParticipantsForEvent(eventId);
        if(memberDTOS.isEmpty()){
            return new ResponseEntity<>("No members found for this event", HttpStatus.NOT_FOUND);

        }
        else{
            return new ResponseEntity<>(memberDTOS, HttpStatus.OK);
        }
    }
    @GetMapping("/{eventId}/rewards")
    public ResponseEntity<?> getRewardsForEvent(@PathVariable Long eventId){
        if(eventId==null){
            return new ResponseEntity<>("Id is required", HttpStatus.BAD_REQUEST);
        }
        List<RewardDTO> rewardDTOS= eventService.getRewardsForEvent(eventId);
        if(rewardDTOS.isEmpty()){
            return new ResponseEntity<>("No rewards found for this event", HttpStatus.NOT_FOUND);

        }
        else{
            return new ResponseEntity<>(rewardDTOS, HttpStatus.OK);
        }
    }


}
