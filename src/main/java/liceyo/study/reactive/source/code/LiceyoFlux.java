package liceyo.study.reactive.source.code;

import org.reactivestreams.Publisher;

import java.util.function.Function;

/**
 * LiceyoFlux
 * @description 流
 * @author lichengyong
 * @date 2019/4/28 19:51
 * @version 1.0
 */
public abstract class LiceyoFlux<T> implements Publisher<T> {

    public static <T>LiceyoFlux<T> just(T...array){
        return new ArrayLiceyoFlux<>(array);
    }

    public <R>LiceyoFlux<R> map(Function<? super T, ? extends R> mapper){
        return new MapLiceyoFlux<>(this, mapper);
    }
}
