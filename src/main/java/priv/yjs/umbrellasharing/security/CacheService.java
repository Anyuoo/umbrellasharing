package priv.yjs.umbrellasharing.security;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * 缓存服务
 *
 * @author Anyu
 * @since 2021/3/25
 */
@Service
public class CacheService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    public Object strGet(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void strPut(String key, Object data) {
        redisTemplate.opsForValue().set(key, data, Duration.ofDays(2));
    }
}
