package liceyo.study.reactive.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * GlobalException
 * @description 全局异常类
 * @author lichengyong
 * @date 2019/4/30 15:32
 * @version 1.0
 */
public class GlobalException extends ResponseStatusException {

    public GlobalException(HttpStatus status) {
        super(status);
    }

    public GlobalException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public GlobalException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }
}
