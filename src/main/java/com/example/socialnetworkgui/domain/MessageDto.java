package com.example.socialnetworkgui.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDto {
    protected Long from;
    protected List<Long> to;
    protected String message;
    protected LocalDateTime data;
    protected List<MessageDto> messages = new ArrayList<>();
    protected String groupName;

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

    public List<Long> getGroup(){
        List<Long> member = new ArrayList<>();
        member.addAll(this.getTo());
        member.add(from);
        return member;
    }

    public List<MessageDto> getConversation(){
        return messages;

    }

    public List<MessageDto> addMessage(MessageDto messageDto){
        messages.add(messageDto);
        return messages;
    }

    public List<MessageDto> setMessages(List<MessageDto> messageDtos){
        messages = messageDtos;
        return messages;
    }

    public String getGroupName(){
        return groupName;
    }

    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
}