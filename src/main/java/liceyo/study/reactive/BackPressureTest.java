package liceyo.study.reactive;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * BackPressureTest
 * @description 回压测试
 * @author lichengyong
 * @date 2019/4/22 15:08
 * @version 1.0
 */
public class BackPressureTest {
    public static void main(String[] args) throws InterruptedException {
//        CountDownLatch latch=new CountDownLatch(1);
//        Flux.just(1,2,3,4)
//                .delayElements(Duration.ofSeconds(1))
//                .doOnComplete(latch::countDown)
//                .subscribe(System.out::println);
//        latch.await();
        backPressureTest01();
    }

    public static void backPressureTest01() throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(1);
        MyEventSource eventSource=new MyEventSource();
        Flux<MyEventSource.MyEvent> flux = Flux.create(sink -> eventSource.register(new MyEventListener() {
            @Override
            public void onNewEvent(MyEventSource.MyEvent event) {
                System.out.println("publish >>> " + event.getMessage());
                sink.next(event);
            }

            @Override
            public void onEventStopped() {
                sink.complete();
            }
        }));
        flux.onBackpressureBuffer(2)
                .doOnRequest(n -> System.out.println("         ===  doOnRequest: " + n + " ==="))
                .publishOn(Schedulers.newSingle("newSingle"), 2)
                .subscribe(new BaseSubscriber<MyEventSource.MyEvent>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(MyEventSource.MyEvent event) {
                        System.out.println("                      hookOnNext <<< " + event.getMessage());
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                        }
                        request(2);
                    }

                    @Override
                    protected void hookOnComplete() {
                        super.hookOnComplete();
                        latch.countDown();
                    }

                    @Override
                    protected void hookOnError(Throwable throwable) {
                        super.hookOnError(throwable);
                        System.out.println("hookOnError");
                    }
                });
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
            }
            eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "Event-" + i));
        }
        eventSource.eventStopped();
        latch.await(1,TimeUnit.MINUTES);
    }
}
