package com.csi.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class MybatisJavaConfig {

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);//dataSource 搞里头

        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true);//开启驼峰
        configuration.setLogImpl(Slf4jImpl.class);//开启日志
        configuration.setAutoMappingBehavior(AutoMappingBehavior.FULL);//开启全自动映射
        sqlSessionFactoryBean.setConfiguration(configuration);//配置项 搞里头

        //sqlSessionFactoryBean.setTypeAliasesPackage("com.csi.domain");//包别名 搞里头

        PageInterceptor pageInterceptor = new PageInterceptor();//分页拦截器

        Properties properties = new Properties();//属性

        properties.setProperty("helperDialect","mysql");//设置对mysql分页
        pageInterceptor.setProperties(properties);//给分页拦截器设置属性
        sqlSessionFactoryBean.setPlugins(pageInterceptor);//分页拦截器 搞里头

        return sqlSessionFactoryBean;

    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){//mapper扫描配置
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("com.csi.mapper");//设置扫描基本包
        return mapperScannerConfigurer;
    }

}
