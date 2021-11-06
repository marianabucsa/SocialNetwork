package ro.ubbcluj.map.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {
    private String firstName;
    private String lastName;
    private String email;
    private List<User> friends;

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
    }

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
     * @return - a list of friends, list
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * adds a friend to the user's list
     *
     * @param u - a user
     */
    public void addFriend(User u) {
        if(friends==null)
            friends=new ArrayList<>();
        this.friends.add(u);
    }

    /**
     * removes a friend from the user's list
     *
     * @param us - a user
     */
    public void removeFriend(User us) {
        if (friends != null)
            friends.removeIf(user -> user.equals(us));
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
     * @return- a hash cade
     */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, friends);
    }

    /**
     * creates a string from the object's attributes
     *
     * @return- a string
     */
    @Override
    public String toString() {
        return "User{" +"ID= "+ getId()+'\''+
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
