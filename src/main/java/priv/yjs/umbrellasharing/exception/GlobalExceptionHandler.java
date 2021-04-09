package priv.yjs.umbrellasharing.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;
import priv.yjs.umbrellasharing.common.CommonResult;
import priv.yjs.umbrellasharing.common.ResultType;


/**
 * 统一异常处理
 *
 * @author Anyu
 * @since 2021/3/25
 */
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    /**
     * 提供对标准Spring MVC异常的处理 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler handleException()}
     *
     * @param ex      the target exception
     * @param request the current request
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CommonResult<Void>> exceptionHandler(Exception ex, WebRequest request) {
        log.info("ExceptionHandler: {}", ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof GlobalException) {
            return handleGlobalException((GlobalException) ex, headers, request);
        }
        if (ex instanceof InternalAuthenticationServiceException) {
            return handleIASE((InternalAuthenticationServiceException) ex, headers, request);
        }
        if (ex instanceof BindException) {
            return handleBindException((BindException) ex, headers, request);
        }
        // notes:  这里可以自定义其他的异常拦截
        return this.handleException(ex, headers, request);
    }

    protected ResponseEntity<CommonResult<Void>> handleBindException(BindException ex, HttpHeaders headers, WebRequest request) {
        var errors = ex.getBindingResult().getAllErrors();
        String message = "参数错误";
        if (!errors.isEmpty()) {
            message = errors.get(0).getDefaultMessage();
        }
        CommonResult<Void> body = new CommonResult<>(500, message);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    protected ResponseEntity<CommonResult<Void>> handleIASE(InternalAuthenticationServiceException ex, HttpHeaders headers, WebRequest request) {
        CommonResult<Void> body = new CommonResult<>(ResultType.AUTH_ERROR.getCode(), ex.getMessage());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * 对GlobalException类返回返回结果的处理
     */
    protected ResponseEntity<CommonResult<Void>> handleGlobalException(GlobalException ex, HttpHeaders headers, WebRequest request) {
        CommonResult<Void> body = CommonResult.with(ex);
        return handleExceptionInternal(ex, body, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * 异常类的统一处理
     */
    protected ResponseEntity<CommonResult<Void>> handleException(Exception ex, HttpHeaders headers, WebRequest request) {
        CommonResult<Void> body = CommonResult.with(ResultType.SYSTEM_ERROR);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    /**
     * org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
     * <p>
     * A single place to customize the response body of all exception types.
     * <p>The default implementation sets the {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}
     * request attribute and creates a {@link ResponseEntity} from the given
     * body, headers, and status.
     */
    protected ResponseEntity<CommonResult<Void>> handleExceptionInternal(
            Exception ex, CommonResult<Void> body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(body, headers, status);
    }
}
