package com.khoa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.google.gson.Gson;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.khoa.config", "com.khoa.controller", "com.khoa.service" })
@PropertySource(value = { "classpath:elasticsearch-config.properties", "classpath:other-config.properties", "classpath:web-config.properties" })
public class SpringConfig extends WebMvcConfigurerAdapter {

    @Value("${PATH_HTML:D:/directoryHTML/}")
    public String PATH_HTML;

    @Value("${MAX_SIZE_UPLOAD:5000000}")
    public long MAX_SIZE_UPLOAD;

    @Value("${PATH_IMAGES:D:/directoryImagesPath/}")
    public String PATH_IMAGES;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/src/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/html/**").addResourceLocations("file:"+PATH_HTML).setCachePeriod(3600).resourceChain(true)
                .addResolver(new GzipResourceResolver());
        registry.addResourceHandler("/images/**").addResourceLocations("file:"+PATH_IMAGES).setCachePeriod(3600).resourceChain(true)
                .addResolver(new GzipResourceResolver());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver commonsMultiart() {
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(MAX_SIZE_UPLOAD);
        return commonsMultipartResolver;
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    /*
     * PropertySourcesPlaceHolderConfigurer Bean only required for @Value("{}")
     * annotations. Remove this bean if you are not using @Value annotations for
     * injecting properties.
     */
    @Bean(name ="pro")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
