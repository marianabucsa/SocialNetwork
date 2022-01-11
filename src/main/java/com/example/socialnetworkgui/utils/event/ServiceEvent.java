package com.example.socialnetworkgui.utils.event;

public class ServiceEvent<E> implements Event{
    private EventType eventType;
    private E data, oldData;

    public ServiceEvent(EventType eventType,E data){
        this.eventType=eventType;
        this.data=data;
    }

    public ServiceEvent(EventType eventType, E data, E oldData){
        this.eventType=eventType;
        this.data=data;
        this.oldData=oldData;
    }


    public EventType getEventType() {
        return eventType;
    }

    public E getData() {
        return data;
    }

    public E getOldData() {
        return oldData;
    }
}
