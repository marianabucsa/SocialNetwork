package ro.ubbcluj.map.domain;

import java.util.List;

public class ReplyMessage extends Message {
    private Long messageToReplay;

    public ReplyMessage(Long from, List<Long> to, String message,Long messageToReplay) {
        super(from, to, message);
        this.messageToReplay=messageToReplay;
    }

    @Override
    public Long getFrom() {
        return super.getFrom();
    }

    @Override
    public void setFrom (Long from) {
        super.setFrom(from);
    }

    @Override
    public List<Long> getTo() {
        return super.getTo();
    }

    @Override
    public void setTo(List<Long> to) {
        super.setTo(to);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public Long getMessageToReplay() {
        return messageToReplay;
    }

    public void setMessageToReplay(Long messageToReplay) {
        this.messageToReplay = messageToReplay;
    }
}
