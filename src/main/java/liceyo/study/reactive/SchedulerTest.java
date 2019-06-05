package liceyo.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.lang.System.out;
/**
 * SchedulerTest
 * @description 调度测试
 * @author lichengyong
 * @date 2019/4/23 13:50
 * @version 1.0
 */
public class SchedulerTest {

    public static void main(String[] args) throws InterruptedException {
        new SchedulerTest().subscribeOn();
    }

    /**
     * SchedulerTest
     * subscribeOn是从subscribe开始往上订阅数据（request），调度于指定的线程
     * 向上订阅过程中遇到其他的subscribeOn，会改变调度器。
     * 从onNext开始向下响应订阅请求时不会改变调度器，直到遇到publishOn改变调度器
     * @description 测试subscribeOn和publishOn
     * @author lichengyong
     * @date 2019/4/30 11:05
     * @version 1.0
     */
    void subscribeOn() throws InterruptedException {
        final CountDownLatch latch=new CountDownLatch(1);
        Flux<Integer> generate = Flux.generate(() -> 0, (count, sink) -> {
            sink.next(++count);
            out.println(Thread.currentThread().getName() + "[generate]:" + count);
            if (count > 5) {
                sink.complete();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return count;
        });
        generate
                .publishOn(Schedulers.elastic())
                .flatMap(i->{
                    out.println(Thread.currentThread().getName()+"[flatMap]:"+i);
                    //读取环境变量（只针对当前Mono或者Flux）
                    return Mono.subscriberContext().map(ctx -> ctx.get("key") + " No." + i);
                })
                .subscribeOn(Schedulers.single())
                .doOnComplete(latch::countDown)
                .subscribeOn(Schedulers.parallel())
                //设置环境变量（只针对当前Mono或者Flux）
                .subscriberContext(ctx->ctx.put("key","Hello"))
                .subscribe(i-> out.println(Thread.currentThread().getName()+"[subscribe]:"+i));
        latch.await();
    }
}
