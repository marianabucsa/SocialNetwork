package ro.ubbcluj.map.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    protected Long from;
    protected List<Long> to;
    protected String message;
    protected LocalDateTime data;

    public Message(Long from, List<Long> to, LocalDateTime data, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }

    public Message(Long from, List<Long> to,  String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = null;
    }
    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public List<Long> getTo() {
        return to;
    }

    public void setTo(List<Long> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        if (!super.equals(o)) return false;
        Message message1 = (Message) o;
        return this.getId().equals(message1.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to, message);
    }

    @Override
    public String toString() {
        return "Message:\n" +
                "From: " + from + "\n" +
                "To: " + to + "\n" +
                "Data: " + data + "\n" +
                "Message= " + message + "\n";
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
