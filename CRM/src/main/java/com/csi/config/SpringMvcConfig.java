package com.csi.config;

/*import com.csi.interceptor.TokenInterceptor;*/
import com.csi.interceptor.TokenAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebMvc
@ComponentScan({"com.csi.controller","com.csi.util"})
public class SpringMvcConfig implements WebMvcConfigurer {

    @Bean
    public HandlerMapping handlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public HandlerAdapter handlerAdapter(){
        return new RequestMappingHandlerAdapter();
    }

    /*@Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        //  /index.jsp
        registry.jsp("/", ".jsp");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        //如果找到的是一个资源地址，则直接放行
        configurer.enable();
    }*/

    // 1. 注册Token拦截器（把拦截器交给Spring管理）
    @Bean
    public TokenAuthInterceptor tokenInterceptor() {
        return new TokenAuthInterceptor();
    }

    // 2. 配置拦截器：指定拦截哪些请求、放行哪些请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor())
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/market/qrcode/common");
    }

    /*//设置媒体字符类型 utf-8
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<org.springframework.http.MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(org.springframework.http.MediaType.TEXT_HTML);
        supportedMediaTypes.add(org.springframework.http.MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(org.springframework.http.MediaType.ALL);
        converters.add(stringConverter);
    }*/

}
