package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDto {
    protected Long from;
    protected List<Long> to;
    protected String message;
    protected LocalDateTime data;

    public MessageDto(Long from, List<Long> to, String message, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getData() {
        return data;
    }
    public void setFrom(Long From){this.from=From;}
    public void setMessage(String Message){this.message=Message;}
}
