package liceyo.study.reactive.source.code;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * ArrayLiceyoFlux
 * @description 数组FLux
 * @author lichengyong
 * @date 2019/4/28 19:50
 * @version 1.0
 */
public class ArrayLiceyoFlux<T> extends LiceyoFlux<T> {

    private T[] array;

    public ArrayLiceyoFlux(T[] array){
        this.array=array;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new ArraySubscription<T>(subscriber,array));
    }

    static class ArraySubscription<T> implements Subscription {
        final Subscriber<? super T> actual;
        final T[] array;
        int index=0;
        boolean canceled=false;

        public ArraySubscription(Subscriber<? super T> actual, T[] array) {
            this.actual = actual;
            this.array = array;
        }

        @Override
        public void request(long l) {
            if (canceled) {
                return;
            }
            long length = array.length;
            for (int i = 0; i < l && index < length; i++) {
                actual.onNext(array[index++]);
            }
            if (index == length) {
                actual.onComplete();
            }
        }

        @Override
        public void cancel() {
            this.canceled=true;
        }
    }
}
