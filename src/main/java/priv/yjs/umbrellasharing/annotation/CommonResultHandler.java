package priv.yjs.umbrellasharing.annotation;

import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ResponseBody
public @interface CommonResultHandler {
}
