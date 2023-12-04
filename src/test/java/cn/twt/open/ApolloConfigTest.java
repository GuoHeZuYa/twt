package cn.twt.open;

import cn.twt.open.cache.redis.RedisCacheManager;
import cn.twt.open.config.ApolloConfigManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ApolloConfigTest {

    @Resource
    ApolloConfigManager apolloConfigManager;

    @Resource
    RedisCacheManager redisCacheManager;

    @Test
    void testConfig() {
        Boolean res = redisCacheManager.set("a",1);
        Integer value = redisCacheManager.get("a", Integer.class);
        System.out.println(res);
        System.out.println(value);
    }
}
