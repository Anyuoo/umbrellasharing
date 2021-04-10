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

    public static <T> CommonResult<T> with(ResultType resultType) {
        return with(resultType, null);
    }

    public static CommonResult<Void> with(GlobalException exception) {
        return new CommonResult<>(exception.getCode(), exception.getMessage());
    }

    public static CommonResult<Void> success() {
        return with(ResultType.SUCCESS, null);
    }

    public static <T> CommonResult<T> success(T data) {
        return with(ResultType.SUCCESS, data);
    }

    public static <T> CommonResult<T> fail() {
        return with(ResultType.FAIL, null);
    }



    /**
     *对于结果是bool值处理
     * @param funcResult 服务处理结果
     */
    public static CommonResult<Void> handleFuncForBool(boolean funcResult) {
        return funcResult ? success() : fail();
    }

    /**
    *业务结果统一封装
    * @author Anyu
    * @since 2021/4/10
    */
    public static <T> CommonResult<T> handleForResult(Supplier<T> supplier) {
        return handleResult(supplier.get());
    }

    public static <A, R> CommonResult<R> handleForResult(A agr, Function<A, R> function) {
        return handleResult(function.apply(agr));
    }

    public static <T> CommonResult<T> handleResult(T result) {
        if (Objects.isNull(result)) {
            return fail();
        }
        //返回结果为ResultType
        if (result instanceof ResultType) {
            return with((ResultType) result);
        }
        //Optional
        if (result instanceof Optional) {
            @SuppressWarnings("unchecked")
            Optional<T> r = (Optional<T>) result;
            return r.map(CommonResult::success).orElseGet(CommonResult::fail);
        }
        //其他
        return success(result);
    }
}
