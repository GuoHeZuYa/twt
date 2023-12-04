package cn.twt.open.cache.redis;

import cn.twt.open.cache.Cache;
import cn.twt.open.exception.RedisKeyEmptyException;
import cn.twt.open.exception.RedisNegativeOffsetException;
import cn.twt.open.exception.RedisValueEmptyException;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lino
 */
@Component
public class RedisCacheManager implements Cache {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean expire(String key, long expire) {
        if (expire < 0) {
            return true;
        }
        try {
            return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("expire error, key: {}, expire: {}", key, expire, e);
            return false;
        }
    }

    @Override
    public Long getExpire(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisKeyEmptyException("getExpire key 不能为空");
        }
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public Boolean existKey(String key) {
        return StringUtils.isNotEmpty(key) && Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false);
    }

    @Override
    public Boolean set(String key, Object value) {
        return set(key,value,-1);
    }

    @Override
    public Boolean set(String key, Object value, long expire) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisKeyEmptyException("set key 不能为空");
        }
        if (Objects.isNull(value)) {
            throw new RedisValueEmptyException("set value 不能为null");
        }
        try {
            log.info("redis set key: {}, value: {}, expire: {} seconds", key, value, expire);
            // expire < 0 永不失效
            if (expire < 0) {
                redisTemplate.opsForValue().set(key,value);
                return true;
            }
            redisTemplate.opsForValue().set(key,value,expire,TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error("set exception, key: {}, value: {}, expire: {} seconds", key, value, expire, e);
            return false;
        }
    }

    @Override
    public Long del(String... key) {
        try {
            if (Objects.isNull(key)) {
                throw new RedisKeyEmptyException("delete key 不能为空");
            }
            List<String> keys = Lists.newArrayList(key).stream().filter(Objects::nonNull).collect(Collectors.toList());
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            log.error("delete key error key: {}", Arrays.toString(key), e);
            return NumberUtils.LONG_ZERO;
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisKeyEmptyException("get key 不能为空");
        }
        Object value = redisTemplate.opsForValue().get(key);
        try {
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            return null;
        } catch (Exception e) {
            log.error("get error, key: {}", key, e);
            return null;
        }
    }

    @Override
    public Long incr(String key, Long offset) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisKeyEmptyException("incr key 不能为空");
        }
        if (offset < 0) {
            throw new RedisNegativeOffsetException();
        }
        try {
            return redisTemplate.opsForValue().increment(key,offset);
        } catch (Exception e) {
            log.error("incr exception, key: {}", key, e);
            return 0L;
        }
    }

    @Override
    public Long decr(String key, Long offset) {
        if (StringUtils.isEmpty(key)) {
            throw new RedisKeyEmptyException("incr key 不能为空");
        }
        if (offset < 0) {
            throw new RedisNegativeOffsetException();
        }
        try {
            return redisTemplate.opsForValue().decrement(key,offset);
        } catch (Exception e) {
            log.error("decr exception, key: {}", key, e);
            return 0L;
        }
    }

    @Override
    public Boolean hset(String key, String field, Object value) {
        return null;
    }

    @Override
    public Boolean hset(String key, String field, Object value, Long expire) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(field)) {
            throw new RedisKeyEmptyException("hset key 或 field 不能为空");
        }
        if (Objects.isNull(value)) {
            throw new RedisValueEmptyException("hset value 不能为null");
        }
        try {
            log.info("redis hset key: {}, field: {}, value: {}, expire: {} seconds", key, field, value, expire);
            // expire < 0 永不失效
            if (expire < 0) {
                redisTemplate.opsForHash().put(key, field, value);
                return true;
            }
            redisTemplate.opsForHash().put(key,field, value);
            expire(key, expire);
            return true;
        } catch (Exception e) {
            log.error("hset exception, key: {}, field: {}, value: {}, expire: {} seconds", key, field, value, expire, e);
            return false;
        }
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        return null;
    }

    @Override
    public Integer hdel(String key, String... field) {
        return null;
    }

    @Override
    public Boolean lset(String key, Object value) {
        return null;
    }

    @Override
    public Boolean lset(String key, Object value, Long expire) {
        return null;
    }

    @Override
    public Boolean lsetAll(String key, List<?> value) {
        return null;
    }

    @Override
    public Boolean lsetAll(String key, List<?> value, Long expire) {
        return null;
    }
}
