package com.csi.config;

/*import com.csi.interceptor.TokenInterceptor;*/
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;



@Configuration
@EnableWebMvc
@ComponentScan({"com.csi.controller","com.csi.utils"})
public class SpringMvcConfig implements WebMvcConfigurer {

    @Bean
    public HandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }

    /*// 1. 注册Token拦截器（把拦截器交给Spring管理）
    @Bean
    public TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }

    // 2. 配置拦截器：指定拦截哪些请求、放行哪些请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(); // 放行不需要登录的接口（和拦截器中一致）
    }*/

}
