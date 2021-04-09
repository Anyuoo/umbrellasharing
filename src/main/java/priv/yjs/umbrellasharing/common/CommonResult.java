package priv.yjs.umbrellasharing.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import priv.yjs.umbrellasharing.exception.GlobalException;

/**
 * 统一结果
 *
 * @author Anyu
 * @since 2021/3/27
 */
@RequiredArgsConstructor
@Getter
public class CommonResult<T> {
    private final Integer code;
    private final String message;
    private final T data;

    public CommonResult(Integer code, String message) {
        this(code, message, null);
    }

    public static <T> CommonResult<T> with(ResultType resultType, T data) {
        return new CommonResult<>(resultType.getCode(), resultType.getMessage(), data);
    }

    public static CommonResult<Void> with(ResultType resultType) {
        return with(resultType, null);
    }

    public static CommonResult<Void> with(GlobalException exception) {
        return new CommonResult<>(exception.getCode(), exception.getMessage());
    }
}
