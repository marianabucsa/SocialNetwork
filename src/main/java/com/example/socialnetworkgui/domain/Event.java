package com.example.socialnetworkgui.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Event extends Entity<Long>{
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String location;
    private Long organizer;
    private List<Long> participants;

    public Event(String name, LocalDateTime startDate, LocalDateTime endDate, String description, String location, Long organizer, List<Long> participants) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.location = location;
        this.organizer = organizer;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Long organizer) {
        this.organizer = organizer;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
