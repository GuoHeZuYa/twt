package cn.twt.open.cache;


import java.util.List;

/**
 * @author Lino
 */
public interface Cache {

    /**
     * 使key过期
     * @param key
     * @param expire
     * @return
     */
    Boolean expire(String key, long expire);

    /**
     * 返回key的过期时间，0代表永久有效
     * @param key
     * @return
     */
    Long getExpire(String key);

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    Boolean existKey(String key);

    /**
     * 普通set 不过期
     * @param key
     * @param value
     * @return Boolean
     */
    Boolean set(String key, Object value);

    /**
     * 普通set，带过期时间
     * @param key
     * @param value
     * @param expire 单位：秒 小于0则永不过期
     * @return Boolean
     */
    Boolean set(String key, Object value, long expire);

    /**
     * 删除key
     * @param key
     * @return
     */
    Long del(String... key);

    /**
     * 普通获取value
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 增加
     * @param key
     * @param offset
     * @return
     */
    Long incr(String key, Long offset);

    /**
     * 减少
     * @param key
     * @param offset
     * @return
     */
    Long decr(String key, Long offset);

    /**
     * 哈希set不过期
     * @param key
     * @param field
     * @param value
     * @return
     */
    Boolean hset(String key, String field, Object value);

    /**
     * 哈希set 过期时间
     * @param key
     * @param field
     * @param value
     * @param expire
     * @return
     */
    Boolean hset(String key, String field, Object value, Long expire);

    /**
     * 哈希get
     * @param key
     * @param field
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T hget(String key, String field, Class<T> clazz);

    /**
     * 删除hash值
     * @param key
     * @param field
     * @return
     */
    Integer hdel(String key, String... field);

    /**
     * list set
     * @param key
     * @param value
     * @return
     */
    Boolean lset(String key, Object value);

    /**
     * list set 带过期时间
     * @param key
     * @param value
     * @param expire
     * @return
     */
    Boolean lset(String key, Object value, Long expire);

    /**
     * list set all
     * @param key
     * @param value
     * @return
     */
    Boolean lsetAll(String key, List<?> value);

    /**
     * list set all 带过期时间
     * @param key
     * @param value
     * @param expire
     * @return
     */
    Boolean lsetAll(String key, List<?> value, Long expire);

}
