package liceyo.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

/**
 * ParallelTest
 * @description 并行测试
 * @author lichengyong
 * @date 2019/4/30 11:40
 * @version 1.0
 */
public class ParallelTest {
    public static void main(String[] args) throws InterruptedException {
        Hooks.onOperatorDebug();
        new ParallelTest().parallelTest1();
    }

    void parallelTest1() {
        Flux<Integer> generate = Flux.generate(() -> 0, (count, sink) -> {
            sink.next(++count);
            if (count > 50) {
                sink.complete();
            }
            return count;
        });
        //将负载划分到多个执行“轨道”上,让订阅的元素均匀分布到轨道线程上
        generate.log().concatWith(Flux.just(100,200,300,400))
                .parallel(8)
                .runOn(Schedulers.elastic())
                .log()
                .sequential()
                .subscribe(out::println);
    }
}
