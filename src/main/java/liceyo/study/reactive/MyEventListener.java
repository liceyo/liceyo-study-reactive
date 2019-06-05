package liceyo.study.reactive;

/**
 * MyEventListener
 * @description 事件监听
 * @author lichengyong
 * @date 2019/4/22 15:59
 * @version 1.0
 */
public interface MyEventListener {
    void onNewEvent(MyEventSource.MyEvent event);
    void onEventStopped();
}
