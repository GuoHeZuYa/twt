package cn.twt.open.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Lino
 */
@Configuration
@Slf4j
public class AsyncConfig {
    private static final int CORES = Runtime.getRuntime().availableProcessors();

    @Bean("asyncTaskExecutor")
    public Executor getExecutor(){
        log.info("cpu cores="+CORES);
        return new ThreadPoolExecutor(CORES, CORES*2,
                5, TimeUnit.SECONDS,new LinkedBlockingDeque<>(1024),
                new BasicThreadFactory.Builder().namingPattern("async-task-thread-pool-%d").daemon(true).build());
    }
}
