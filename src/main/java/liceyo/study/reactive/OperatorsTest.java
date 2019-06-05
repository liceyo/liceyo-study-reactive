package liceyo.study.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static java.lang.System.out;

/**
 * OperatorsTest
 * @description 操作符测试
 * @author lichengyong
 * @date 2019/4/30 14:13
 * @version 1.0
 */
public class OperatorsTest {
    public static void main(String[] args) {
        OperatorsTest test = new OperatorsTest();
        test.testThen();
    }

    void testThen(){
        Flux.just(1,2,3)
                .log()
                .then(Mono.just("over"))
                .thenReturn("xxxxxx")
                .subscribe(out::println);
    }

}
