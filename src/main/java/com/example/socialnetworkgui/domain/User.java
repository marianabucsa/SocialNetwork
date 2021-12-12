package com.example.socialnetworkgui.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private List<Long> friends;
    private String password;

    /**
     * user constructor , creates a user
     *
     * @param firstName - first name of the user
     * @param lastName  - last name of the user
     * @param email     - email of the user
     */
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password=null;
    }

    /**
     * Setter methode for password
     * @param password password of the user, string
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     *  getter methode for password
     * @return user password, string
     */
    public String getPassword(){return password;}

    /**
     * getter methode for firstName
     *
     * @return - first name of the user, string
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * setter methode for firstName
     *
     * @param firstName - a first name ,string
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * getter methode for lastName
     *
     * @return - last name of the user, string
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * setter methode for lastName
     *
     * @param lastName - a last name ,string
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * getter methode for email
     *
     * @return - email of the user, string
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter methode for email
     *
     * @param email - a email ,string
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getter methode for friends list
     *
     * @return - a list of user ids, list of friends
     */
    public List<Long> getFriends() {
        return friends;
    }

    /**
     * setter methode for friends list
     *
     * @param friends - a list of user ids, list of long
     */
    public void setFriends(List<Long> friends) {
        this.friends = new ArrayList<>(friends);
    }

    /**
     * adds a friend to the user's list
     *
     * @param id - an id
     */
    public void addFriend(Long id) {
        if (friends == null)
            friends = new ArrayList<>();
        this.friends.add(id);
    }

    /**
     * removes a friend from the user's list
     *
     * @param id - an id
     */
    public void removeFriend(Long id) {
        if (friends != null)
            friends.removeIf(id1 -> id1.equals(id));
    }

    /**
     * removes all friends from the user's list
     */
    public void clearFriends() {
        if (friends != null)
            friends.clear();
    }

    /**
     * verifies if 2 users are equal
     *
     * @param o- a user
     * @return - true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    /**
     * hashing methode for a user
     *
     * @return - a hash cade
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, friends);
    }

    /**
     * creates a string from the object's attributes
     *
     * @return - a string
     */
    @Override
    public String toString() {
        return "User{" + "ID= " + getId() + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
