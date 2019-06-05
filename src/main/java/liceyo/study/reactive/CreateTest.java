package liceyo.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static java.lang.System.out;

/**
 * CreateTest
 * @description 异步创建测试
 * @author lichengyong
 * @date 2019/4/22 16:01
 * @version 1.0
 */
public class CreateTest {

    public static void main(String[] args) throws InterruptedException {
        CreateTest test = new CreateTest();
        test.testCreate();
//        test.testAsyncCreate();
    }

    /**
     * CreateTest
     * @description 创建异步流(不是并行流)
     * @author lichengyong
     * @date 2019/4/22 16:06
     * @version 1.0
     */
    public void testCreate() throws InterruptedException {
        MyEventSource eventSource = new MyEventSource();
        Flux.create(sink -> eventSource.register(new MyEventListener() {
                    @Override
                    public void onNewEvent(MyEventSource.MyEvent event) {
                        sink.next(event);
                    }

                    @Override
                    public void onEventStopped() {
                        sink.complete();
                    }
                })
        )
                .log()
                .subscribe(System.out::println);
        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
            out.println("create->"+i);
            eventSource.newEvent(new MyEventSource.MyEvent(new Date(), "Event-" + i));
        }
        eventSource.eventStopped();
    }

    /**
     * CreateTest
     * @description 异步生成
     * @author lichengyong
     * @date 2019/4/23 13:42
     * @version 1.0
     */
    public void testAsyncCreate() throws InterruptedException {
        CountDownLatch latch=new CountDownLatch(1);
        Mono.fromCallable(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out.println(Thread.currentThread().getName());
            return "Hello, Reactor!";
        })
                .subscribeOn(Schedulers.elastic())
                .publishOn(Schedulers.elastic())
                .map(s->{
                    System.out.println(Thread.currentThread().getName());
                    return s+"xxx";
                })
                .log()
                .subscribe(out::println, null, latch::countDown);
        latch.await();
    }
}
