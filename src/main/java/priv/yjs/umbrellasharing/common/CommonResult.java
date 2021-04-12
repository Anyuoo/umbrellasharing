package priv.yjs.umbrellasharing.common;

import cn.hutool.core.lang.func.Func1;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import priv.yjs.umbrellasharing.exception.GlobalException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 统一结果
 *
 * @author Anyu
 * @since 2021/3/27
 */

@Getter
public class CommonResult<T> {
    private final Integer code;
    private final Boolean success;
    private final String message;
    private final T data;

    public CommonResult(Integer code, Boolean success, String message, T data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public CommonResult(Integer code, String message) {
        this(code, null, message);
    }

    public CommonResult(Integer code, Boolean success, String message) {
        this(code, success, message, null);
    }

    public static <T> CommonResult<T> with(ResultType resultType, T data) {
        return new CommonResult<>(resultType.getCode(),resultType.getSuccess(), resultType.getMessage(), data);
    }

    public static <T> CommonResult<T> with(ResultType resultType) {
        return with(resultType, null);
    }

    public static CommonResult<Void> with(GlobalException exception) {
        return new CommonResult<>(exception.getCode(), exception.getMessage());
    }

    public static CommonResult<Void> succeed() {
        return with(ResultType.SUCCESS, null);
    }

    public static <T> CommonResult<T> succeed(T data) {
        return with(ResultType.SUCCESS, data);
    }

    public static <T> CommonResult<T> failed() {
        return with(ResultType.FAIL, null);
    }



    /**
     *对于结果是bool值处理
     * @param funcResult 服务处理结果
     */
    public static CommonResult<Void> applyByBool(boolean funcResult) {
        return funcResult ? succeed() : failed();
    }

    /**
    *业务结果统一封装
    * @author Anyu
    * @since 2021/4/10
    */
    public static <T> CommonResult<T> applyByData(Supplier<T> supplier) {
        return handleResult(supplier.get());
    }

    public static <T> CommonResult<T> handleResult(T result) {
        if (Objects.isNull(result)) {
            return failed();
        }
        //返回结果为ResultType
        if (result instanceof ResultType) {
            return with((ResultType) result);
        }
        //Optional
        if (result instanceof Optional) {
            @SuppressWarnings("unchecked")
            Optional<T> r = (Optional<T>) result;
            return r.map(CommonResult::succeed).orElseGet(CommonResult::failed);
        }
        //其他
        return succeed(result);
    }
}
