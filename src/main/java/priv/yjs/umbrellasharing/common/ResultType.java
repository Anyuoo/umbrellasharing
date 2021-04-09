package priv.yjs.umbrellasharing.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 操作结果类型
 *
 * @author Anyu
 * @since 2021/3/27
 */
@Getter
@RequiredArgsConstructor
public enum ResultType {
    SUCCESS(true, 2000, "操作成功"),
    FAIL(false, 2001, "操作失败"),
    QUERY_SUCCESS(true, 2003, "查询成功"),
    QUERY_FAIL(false, 2004, "查询失败"),
    SYSTEM_ERROR(false, 2005, "系统错误"),
    AUTH_ERROR(false, 2006, "未认证"),
    USER_EXISTED(null, 2007, "用户已存在"),
    USER_NOT_EXISTED(null, 2008, "用户不存在"),
    SYS_ARGS_ERROR(false, 2009, "系统参数错误"),
    NOT_RETURN(false, 2010, "有未归还记录"),
    NOT_PAY(false, 2011, "有未支付记录"),
    PLACEMENT_NOT_EXISTED(null,2012,"放置点不存在"),
    USER_NOT_LOGIN(null,2013,"用户未登录")
    ;


    private final Boolean success;
    private final Integer code;
    private final String message;

}
