package liceyo.study.reactive.error;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Map;

/**
 * GlobalErrorAttributes
 * @description 全局异常属性配置
 * @author lichengyong
 * @date 2019/4/30 15:34
 * @version 1.0
 */
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> attributes = super.getErrorAttributes(request, includeStackTrace);
        if (getError(request) instanceof GlobalException){
            GlobalException ex = (GlobalException) getError(request);
            attributes.put("exception", ex.getClass().getSimpleName());
            attributes.put("message", ex.getMessage());
            attributes.put("status", ex.getStatus().value());
            attributes.put("error", ex.getStatus().getReasonPhrase());
            return attributes;
        }
        attributes.put("exception", "SystemException");
        attributes.put("message", "System Error , Check logs!");
        attributes.put("status", "500");
        attributes.put("error", " System Error ");
        return attributes;
    }
}
