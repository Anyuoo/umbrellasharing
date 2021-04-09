package priv.yjs.umbrellasharing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import priv.yjs.umbrellasharing.common.ResultType;

/**
 * 全局异常
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Getter
public class GlobalException extends RuntimeException {
    private final Integer code;
    private final HttpStatus httpStatus;

    public GlobalException(String message, Integer code, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public static GlobalException causeBy(ResultType resultType) {
        return causeBy(resultType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static GlobalException causeBy(ResultType resultType, HttpStatus httpStatus) {
        return new GlobalException(resultType.getMessage(), resultType.getCode(), httpStatus);
    }

}
