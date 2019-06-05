package liceyo.study.reactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * WebFluxConfig
 * @description webflux配置
 * @author lichengyong
 * @date 2019/4/24 15:00
 * @version 1.0
 */
@Configuration
public class WebFluxConfig implements WebFluxConfigurer {
    /**
     * WebFluxConfig
     * @description 跨域请求配置
     * @author lichengyong
     * @date 2019/4/24 15:01
     * @param registry 跨域注册
     * @version 1.0
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .exposedHeaders(HttpHeaders.SET_COOKIE);
    }
}
