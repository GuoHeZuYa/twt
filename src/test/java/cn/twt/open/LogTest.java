package cn.twt.open;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LogTest {
    @Test
    void test1(){
        Logger logger = LoggerFactory.getLogger(LogTest.class);

        logger.info("info");
        logger.error("error");
        logger.warn("warn");
    }
}
