package priv.yjs.umbrellasharing.common;

import java.util.function.Predicate;

@FunctionalInterface
public interface Service <T>{
    ResultType service(Predicate<T> predicate);
}
