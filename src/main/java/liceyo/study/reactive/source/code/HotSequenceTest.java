package liceyo.study.reactive.source.code;

import reactor.core.Disposable;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * HotSequenceTest
 *
 * “热”序列，它直接在组装期就拿到数据，如果之后有谁订阅它，就重新发送数据给订阅者。
 * @description 热序列测试
 * @author lichengyong
 * @date 2019/4/30 14:49
 * @version 1.0
 */
public class HotSequenceTest {
    public static void main(String[] args) throws InterruptedException {
        HotSequenceTest test = new HotSequenceTest();
        test.testRefCount();
    }

    void testHot(){
        UnicastProcessor<String> hotSource = UnicastProcessor.create();
        Flux<String> hotFlux = hotSource.publish()
                .autoConnect()
                .map(String::toUpperCase);
        hotFlux.subscribe(d -> System.out.println("Subscriber 1 to Hot Source: "+d));
        hotSource.onNext("blue");
        hotSource.onNext("green");
        hotFlux.subscribe(d -> System.out.println("Subscriber 2 to Hot Source: "+d));
        hotSource.onNext("orange");
        hotSource.onNext("purple");
        hotSource.onComplete();
    }

    void testConnect() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("上游收到订阅"));
        ConnectableFlux<Integer> co = source.publish();
        co.subscribe(System.out::println, e -> {}, () -> {});
        co.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("订阅者完成订阅操作");
        Thread.sleep(500);
        System.out.println("还没有连接上");
        co.connect();
    }

    void testAutoConnect() throws InterruptedException {
        Flux<Integer> source = Flux.range(1, 3)
                .doOnSubscribe(s -> System.out.println("上游收到订阅"));
        // 需要两个订阅者才自动连接
        Flux<Integer> autoCo = source.publish().autoConnect(2);
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
        System.out.println("第一个订阅者完成订阅操作");
        Thread.sleep(500);
        System.out.println("第二个订阅者完成订阅操作");
        autoCo.subscribe(System.out::println, e -> {}, () -> {});
    }

    void testRefCount() throws InterruptedException {
        Flux<Long> source = Flux.interval(Duration.ofMillis(500))
                .doOnSubscribe(s -> System.out.println("上游收到订阅"))
                .doOnCancel(() -> System.out.println("上游发布者断开连接"));
        Flux<Long> refCounted = source.publish().refCount(2, Duration.ofSeconds(2));
        System.out.println("第一个订阅者订阅");
        Disposable sub1 = refCounted.subscribe(l -> System.out.println("sub1: " + l));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第二个订阅者订阅");
        Disposable sub2 = refCounted.subscribe(l -> System.out.println("sub2: " + l));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第一个订阅者取消订阅");
        sub1.dispose();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第二个订阅者取消订阅");
        sub2.dispose();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第三个订阅者订阅");
        Disposable sub3 = refCounted.subscribe(l -> System.out.println("sub3: " + l));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第三个订阅者取消订阅");
        sub3.dispose();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("第四个订阅者订阅");
        Disposable sub4 = refCounted.subscribe(l -> System.out.println("sub4: " + l));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("第五个订阅者订阅");
        Disposable sub5 = refCounted.subscribe(l -> System.out.println("sub5: " + l));
        TimeUnit.SECONDS.sleep(2);
    }
}
