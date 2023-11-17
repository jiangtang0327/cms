package com.pakho.cms.config;

import com.pakho.cms.web.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public void addInterceptors(InterceptorRegistry registry) {
        // 对swagger的请求不进行拦截
//        String[] excludePatterns = new String[]{
//                "/profile/**",
//                "/common/download**",
//                "/common/download/resource**",
//                "/swagger-ui.html",
//                "/swagger-resources/**",
//                "/webjars/**",
//                "/*/api-docs",
//                "/favicon.ico",
//                "/doc.html",
//                "/error"
//        };
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/auth/**")
                .excludePathPatterns("/auth/article/query","/auth/article/queryById/**")
                .excludePathPatterns("/auth/category/queryAllParent",
                        "/auth/comment/queryByArticleId/{id}");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // 允许跨域的头部信息
        config.addAllowedHeader("*");
        // 允许跨域的方法
        config.addAllowedMethod("*");
        // 可访问的外部域
        config.addAllowedOrigin("*");
        // 需要跨域用户凭证（cookie、HTTP认证及客户端SSL证明等）
        //config.setAllowCredentials(true);
        //config.addAllowedOriginPattern("*");
        // 跨域路径配置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
