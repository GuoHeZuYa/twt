package cn.twt.open.utils;

import cn.twt.open.cache.redis.RedisCacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Lino
 */
@Component
public class CacheUtils {

    @Resource
    RedisCacheManager redisCacheManager;

    public Boolean cacheSet(String key, Object value) {
        try {
            return redisCacheManager.set(key, value);
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean cacheSet(String key, Object value, long expire) {
        try {
            return redisCacheManager.set(key, value, expire);
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T cacheGet(String key, Class<T> clazz) {
        try {
            return redisCacheManager.get(key,clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public Long cacheDel(String... key) {
        try {
            return redisCacheManager.del(key);
        } catch (Exception e) {
            return 0L;
        }
    }
}
