package liceyo.study.reactive.source.code;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * Test
 * @description 测试
 * @author lichengyong
 * @date 2019/4/29 9:51
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) {
        LiceyoFlux.just(1,2,3,4)
                .map(i->i*10)
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        this.request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println(value);
                        request(1);
                    }

                    @Override
                    protected void hookOnComplete() {
                        System.out.println("hookOnComplete");
                    }
                });
    }
}
