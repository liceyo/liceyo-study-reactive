package liceyo.study.reactive.api;

import liceyo.study.reactive.MyBean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ReactiveStreamApi
 * @description 响应式流Api
 * @author lichengyong
 * @date 2019/4/23 14:24
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/test")
public class ReactiveStreamApi {

    private static Flux<String> flux=Flux.just("a","b","c");


    @PostMapping("")
    public Mono<Boolean> add(String value){
        flux = ReactiveStreamApi.flux.concatWithValues(value);
        return flux.hasElement(value);
    }

    @DeleteMapping("/{value}")
    public Mono<Boolean> delete(@PathVariable String value){
        flux = ReactiveStreamApi.flux.filter(s -> !s.equals(value));
        return flux.hasElement(value);
    }

    @GetMapping(value = "",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> findAll(){
        return flux.delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/bean", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux findBean(){
        AtomicInteger count=new AtomicInteger(10);
        Flux<Object> flux = Flux.create(sink -> {
            while (count.get() > 0) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MyBean bean = new MyBean("lee", count.decrementAndGet());
                sink.next(bean);
            }
            sink.complete();
        });
        return flux.map(bean-> ServerSentEvent.builder(bean).event("item").build())
                .concatWith(Mono.just(ServerSentEvent.<Object>builder("over").event("over").build()))
                .log();
    }
}
