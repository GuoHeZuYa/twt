package cn.twt.open.utils;



import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lino
 * @date 2020-11-3
 * @description 简单本地全局缓存实现
 */
@Slf4j
public class MapCache {
    private static final int INIT_SIZE = 4096;

    private static volatile MapCache INSTANCE = null;

    private Map<String, CacheObject> cachePool;

    static class CacheObject{
        private final String key;
        private final Object value;
        private final long expire;

        CacheObject(String key, Object value, long expire){
            this.key = key;
            this.value = value;
            this.expire = expire;
        }

        public Object getValue() {
            return value;
        }

        public long getExpire() {
            return expire;
        }

        public String getKey() {
            return key;
        }
    }

    private MapCache(){
        this(INIT_SIZE);
    }

    private MapCache(int size){
        cachePool = new ConcurrentHashMap<>(size);
    }

    public static MapCache getInstance() {
        if (INSTANCE == null){
            synchronized (MapCache.class){
                if (INSTANCE == null){
                    INSTANCE = new MapCache();
                }
            }
        }
        return INSTANCE;
    }

    public boolean set(String key, Object value){
        return set(key,value,0);
    }

    /**
     *
     * @param key 键
     * @param value 值
     * @param expire 过期时间（秒）
     * @return
     */
    public boolean set(String key, Object value, long expire){
        if (isFull()){
            if (deleteAllExpiredEntry()) {
                if (isFull()) {
                    return false;
                }
            }
        }
        expire = expire > 0 ? (System.currentTimeMillis() / 1000)+ expire : expire;
        CacheObject cacheObject = new CacheObject(key, value, expire);
        cachePool.put(key, cacheObject);
        return true;
    }

    public <T> T get(String key){
        CacheObject cacheObject = cachePool.get(key);
        if (null != cacheObject){
            long current = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpire() == 0 || cacheObject.getExpire() > current){
                Object value = cacheObject.getValue();
                return (T) value;
            } else {
                del(key);
            }
        }
        return null;
    }

    public void del(String key){
        cachePool.remove(key);
    }

    private boolean isFull(){
        return cachePool.size() == INIT_SIZE;
    }

    private boolean deleteAllExpiredEntry(){
        try {
            long cur = System.currentTimeMillis()/1000;
            cachePool.entrySet().removeIf(entry->(
                    entry.getValue().getExpire() !=0 && entry.getValue().getExpire() <= cur)
            );
            return true;
        } catch (Exception e){
            log.error("cache delete error", e);
            return false;
        }
    }

}
