package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Long replyingToMessage;

    /**
     * constructor for reply message
     *
     * @param from              - a user id, long
     * @param to                - a list of user ids, list of long
     * @param data              - the date and time when the message was sent, local date time
     * @param message           - the message , string
     * @param replyingToMessage - id of the message to reply, long
     */
    public ReplyMessage(Long from, List<Long> to, LocalDateTime data, String message, Long replyingToMessage) {
        super(from, to, data, message);
        this.replyingToMessage = replyingToMessage;
    }

    /**
     * constructor for reply message
     *
     * @param from              - a user id, long
     * @param to                - a list of user ids, list of long
     * @param message           - the message , string
     * @param replyingToMessage - id of the message to reply, long
     */
    public ReplyMessage(Long from, List<Long> to, String message, Long replyingToMessage) {
        super(from, to, message);
        this.replyingToMessage = replyingToMessage;
    }

    /**
     * getter methode for from
     *
     * @return - an id of a user, long
     */
    @Override
    public Long getFrom() {
        return super.getFrom();
    }

    /**
     * setter methode for from
     *
     * @param from - an id of a user, long
     */
    @Override
    public void setFrom(Long from) {
        super.setFrom(from);
    }

    /**
     * getter methode for to
     *
     * @return - a list of user ids, list of long
     */
    @Override
    public List<Long> getTo() {
        return super.getTo();
    }

    /**
     * setter methode for to
     *
     * @param to - a list of user ids, list of long
     */
    @Override
    public void setTo(List<Long> to) {
        super.setTo(to);
    }

    /**
     * getter methode for data
     *
     * @return - a date and time
     */
    @Override
    public LocalDateTime getData() {
        return super.getData();
    }

    /**
     * getter methode for message
     *
     * @return - a string
     */
    @Override
    public void setData(LocalDateTime data) {
        super.setData(data);
    }

    /**
     * getter methode for message
     *
     * @return - a string
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * setter methode for message
     *
     * @param message - a string
     */
    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }

    /**
     * getter methode for ReplyingToMessage
     *
     * @return - an id of the message to reply, long
     */
    public Long getReplyingToMessage() {
        return replyingToMessage;
    }

    /**
     * setter methode for ReplyingToMessage
     *
     * @param replyingToMessage - an id of the message to reply, long
     */
    public void setReplyingToMessage(Long replyingToMessage) {
        this.replyingToMessage = ReplyMessage.this.replyingToMessage;
    }

    /**
     * verifies if 2 messages are equal
     *
     * @param o - an object
     * @return - true if the messages are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * gives hash code of a message
     *
     * @return - hash code for a message
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * creates a string from the message's attributes
     *
     * @return
     */
    @Override
    public String toString() {
        return super.toString();
    }


}
