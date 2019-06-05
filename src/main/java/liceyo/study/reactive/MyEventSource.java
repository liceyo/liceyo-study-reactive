package liceyo.study.reactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MyEventSource
 * @description 事件源
 * @author lichengyong
 * @date 2019/4/22 15:58
 * @version 1.0
 */
public class MyEventSource {
    private List<MyEventListener> listeners;

    public MyEventSource() {
        this.listeners = new ArrayList<>();
    }

    public void register(MyEventListener listener) {
        listeners.add(listener);
    }

    public void newEvent(MyEvent event) {
        for (MyEventListener listener : listeners) {
            listener.onNewEvent(event);
        }
    }

    public void eventStopped() {
        for (MyEventListener listener : listeners) {
            listener.onEventStopped();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyEvent {
        private Date timestamp;
        private String message;
    }
}
