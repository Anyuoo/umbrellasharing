package priv.yjs.umbrellasharing.common;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地缓存
 *
 * @author Anyu
 * @since 2021/3/24
 */
@Component
public class LocalCache {
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>(256);

    public void put(String key, Object value) {
        cache.put(key, value);
    }

    public Object get(String key) {
        return cache.get(key);
    }
}
