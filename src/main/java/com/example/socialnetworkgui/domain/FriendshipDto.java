package com.example.socialnetworkgui.domain;

public class FriendshipDto {
    private Long idFrom;
    private Long idTo;
    private String firstName;
    private String lastName;
    private String status;
    private String date;

    public FriendshipDto(Long idFrom, Long idTo, String firstName, String lastName, String status, String date) {
        this.idFrom = idFrom;
        this.idTo = idTo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }
}
