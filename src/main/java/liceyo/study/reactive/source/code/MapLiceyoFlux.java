package liceyo.study.reactive.source.code;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * MapLiceyoFlux
 * @description 转换Flux
 * @author lichengyong
 * @date 2019/4/29 14:59
 * @version 1.0
 */
public class MapLiceyoFlux<T,R> extends LiceyoFlux<R> {
    private final LiceyoFlux<? extends T> source;
    private final Function<? super T, ? extends R> mapper;

    public MapLiceyoFlux(LiceyoFlux<? extends T> source, Function<? super T, ? extends R> mapper) {
        this.source = source;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Subscriber<? super R> subscriber) {
        this.source.subscribe(new MapSubscriber<T, R>(subscriber,mapper));
    }

    static final class MapSubscriber<T,R> implements Subscriber<T>, Subscription{
        private final Subscriber<? super R> actual;
        private final Function<? super T, ? extends R> mapper;
        private Subscription subscriptionOfUpstream;

        public MapSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends R> mapper) {
            this.actual = subscriber;
            this.mapper = mapper;
        }

        @Override
        public void onSubscribe(Subscription subscription) {
            this.subscriptionOfUpstream=subscription;
            actual.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            actual.onNext(mapper.apply(t));
        }

        @Override
        public void onError(Throwable throwable) {
            actual.onError(throwable);
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }

        @Override
        public void request(long l) {
            this.subscriptionOfUpstream.request(l);
        }

        @Override
        public void cancel() {
            this.subscriptionOfUpstream.cancel();
        }
    }
}
