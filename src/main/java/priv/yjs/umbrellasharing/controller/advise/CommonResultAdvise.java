package priv.yjs.umbrellasharing.controller.advise;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import priv.yjs.umbrellasharing.annotation.CommonResultHandler;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.ResultType;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

/**
 * 统一结果处理
 *
 * @author Anyu
 * @since 2021/3/24
 */
@RestControllerAdvice
public class CommonResultAdvise implements ResponseBodyAdvice<Object> {
    private static final Class<? extends Annotation> ANNOTATION_TYPE = CommonResultHandler.class;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ANNOTATION_TYPE) || returnType.hasMethodAnnotation(ANNOTATION_TYPE);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        //返回结果为null
        if (Objects.isNull(body)) {
            return CommonResult.with(ResultType.FAIL);
        }
        //返回结果为CommonResult
        if (body instanceof CommonResult) {
            return body;
        }
        //返回结果为ResultType
        if (body instanceof ResultType) {
            return CommonResult.with((ResultType) body);
        }
        //Optional
        if (body instanceof Optional) {
            return CommonResult.with(ResultType.SUCCESS, ((Optional<?>) body).orElse(null));
        }
        //其他
        return CommonResult.with(ResultType.SUCCESS, body);
    }

}
