package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Long replyingToMessage;

    public ReplyMessage(Long from, List<Long> to, LocalDateTime data, String message, Long replyingToMessage) {
        super(from, to, data, message);
        this.replyingToMessage=replyingToMessage;
    }

    public ReplyMessage(Long from, List<Long> to, String message, Long replyingToMessage) {
        super(from, to, message);
        this.replyingToMessage=replyingToMessage;
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

    @Override
    public LocalDateTime getData() {
        return super.getData();
    }

    @Override
    public void setData(LocalDateTime data) {
        super.setData(data);
    }


    public Long getReplyingToMessage() {
        return replyingToMessage;
    }

    public void setReplyingToMessage(Long replyingToMessage) {
        this.replyingToMessage = ReplyMessage.this.replyingToMessage;
    }
}
