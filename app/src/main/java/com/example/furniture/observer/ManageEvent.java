package com.example.furniture.observer;

import java.util.ArrayList;
import java.util.List;

public class ManageEvent {
    private static ManageEvent instance;
    private List<IEventListener> listeners;

    public ManageEvent() {
        this.listeners = new ArrayList<>();
    }

    public static synchronized ManageEvent getInstance() {
        if (instance == null) {
            instance = new ManageEvent();
        }
        return instance;
    }

    public void addListener(IEventListener listener) {
        if (!listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(IEventListener listener) {
        this.listeners.remove(listener);
    }

    public void notifyListeners(boolean success) {
        for (IEventListener listener : listeners) {
            listener.onEvent(success);
        }
    }
}