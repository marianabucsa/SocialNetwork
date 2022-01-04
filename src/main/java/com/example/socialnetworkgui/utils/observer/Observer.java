package com.example.socialnetworkgui.utils.observer;

import com.example.socialnetworkgui.utils.event.Event;

import java.io.IOException;

public interface Observer<E extends Event> {
    void update(E e) throws IOException;
}
