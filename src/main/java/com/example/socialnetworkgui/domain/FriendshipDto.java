package com.example.socialnetworkgui.domain;

public class FriendshipDto {
    private String emailTo;
    private String firstName;
    private String lastName;
    private String date;

    public FriendshipDto( String emailTo, String firstName, String lastName, String date) {
        this.emailTo = emailTo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDate() {
        return date;
    }

    public String getEmailTo() {
        return emailTo;
    }
}
