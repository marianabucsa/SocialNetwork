package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    protected Long from;
    protected List<Long> to;
    protected String message;
    protected LocalDateTime data;

    /**
     * constructor for message
     *
     * @param from    - a user id, long
     * @param to      - a list of user ids, list of long
     * @param data    - the date and time when the message was sent, local date time
     * @param message - the message , string
     */
    public Message(Long from, List<Long> to, LocalDateTime data, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }

    /**
     * constructor for message without the date
     *
     * @param from    - a user id, long
     * @param to      - a list of user ids, list of long
     * @param message - the message , string
     */
    public Message(Long from, List<Long> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = null;
    }

    /**
     * getter methode for from
     *
     * @return - an id of a user, long
     */
    public Long getFrom() {
        return from;
    }

    /**
     * setter methode for from
     *
     * @param from - an id of a user, long
     */
    public void setFrom(Long from) {
        this.from = from;
    }

    /**
     * getter methode for to
     *
     * @return - a list of user ids, list of long
     */
    public List<Long> getTo() {
        return to;
    }

    /**
     * setter methode for to
     *
     * @param to - a list of user ids, list of long
     */
    public void setTo(List<Long> to) {
        this.to = to;
    }

    /**
     * getter methode for data
     *
     * @return - a date and time
     */
    public LocalDateTime getData() {
        return data;
    }

    /**
     * setter methode for data
     *
     * @param data - a LocalDateTime
     */
    public void setData(LocalDateTime data) {
        this.data = data;
    }

    /**
     * getter methode for message
     *
     * @return - a string
     */
    public String getMessage() {
        return message;
    }

    /**
     * setter methode for message
     *
     * @param message - a string
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * verifies if 2 messages are equal
     *
     * @param o - an object
     * @return - true if the messages are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return this.getId().equals(message1.getId());
    }

    /**
     * gives hash code of a message
     *
     * @return - hash code for a message
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, message);
    }

    /**
     * creates a string from the message's attributes
     *
     * @return
     */
    @Override
    public String toString() {
        return "Message:\n" +
                "From: " + from + "\n" +
                "To: " + to + "\n" +
                "Data: " + data + "\n" +
                "Message= " + message + "\n";
    }


}
