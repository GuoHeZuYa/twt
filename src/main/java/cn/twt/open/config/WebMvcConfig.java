package cn.twt.open.config;

import cn.twt.open.interceptor.AccessInterceptor;
import cn.twt.open.interceptor.JwtInterceptor;
import cn.twt.open.interceptor.ManageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lino
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**").excludePathPatterns("/twtopen/pic/**");
        registry.addInterceptor(accessInterceptor()).addPathPatterns("/**").excludePathPatterns("/twtopen/pic/**");
        registry.addInterceptor(manageInterceptor()).addPathPatterns("/**").excludePathPatterns("/twtopen/pic/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将文件路径的请求转发
        registry.addResourceHandler("/twtopen/pic/**")
                .addResourceLocations("file:/home/data/twtopen/pic/");
    }

    /**
     * Jwt拦截器
     * @return JwtInterceptor
     */
    @Bean
    JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }

    @Bean
    AccessInterceptor accessInterceptor(){
        return new AccessInterceptor();
    }

    @Bean
    ManageInterceptor manageInterceptor(){
        return new ManageInterceptor();
    }
}
