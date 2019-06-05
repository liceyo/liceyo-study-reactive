package liceyo.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

import static java.lang.System.out;

/**
 * HooksTest
 * @description 钩子测试
 * @author lichengyong
 * @date 2019/5/5 16:00
 * @version 1.0
 */
public class HooksTest {
    public static void main(String[] args) {
        Hooks.onEachOperator(p->{
            out.println("onEachOperator");
            return p;
        });
        Hooks.onEachOperator("map",p->{
            out.println("mapOnEachOperator");
            return p;
        });
        Flux.just(1,2,3,4)
                .map(i->i*10)
                .filter(i->i>10)
                .subscribe(out::println);
    }
}
