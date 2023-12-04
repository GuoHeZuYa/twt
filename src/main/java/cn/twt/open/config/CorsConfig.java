package cn.twt.open.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Lino
 * 跨域配置
 * 踩坑实记：不要在WebMvcConfig中重写addCorsMappings，有很多bug。最好是单独写一个跨域配置
 * 另外，在涉及header过滤的时候，要在过滤器中放行OPTIONS请求
 */

@Configuration
public class CorsConfig {
    private static final String[] allowOrigins = {
            "https://i.twt.edu.cn",
            "https://selfstudy.twt.edu.cn",
            "https://shijian.twt.edu.cn",
            "https://party.twt.edu.cn",
            "https://notice.twt.edu.cn",
            "http://localhost",
            "http://127.0.0.1"
    };
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许任何域名
//        for (String s: allowOrigins){
//            corsConfiguration.addAllowedOrigin(s);
//        }
        corsConfiguration.addAllowedOrigin("*");
        //允许任何头
        corsConfiguration.addAllowedHeader("*");
        //允许任何方法
        corsConfiguration.addAllowedMethod("*");
        //设置预检有效期
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //注册
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
