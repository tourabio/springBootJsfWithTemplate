package com.example.kidszonea4arctic3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.EnumSet;

import javax.faces.webapp.FacesServlet;
import javax.servlet.DispatcherType;

import org.ocpsoft.rewrite.servlet.RewriteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;


//events
@SpringBootApplication
public class KidsZoneA4arctic3Application {

    public static void main(String[] args) {
        SpringApplication.run(KidsZoneA4arctic3Application.class, args);
    }
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
    FacesServlet servlet = new FacesServlet();
    return new ServletRegistrationBean(servlet, "*.jsf"); }
    @Bean
    public FilterRegistrationBean rewriteFilter() {
    FilterRegistrationBean rwFilter = new FilterRegistrationBean(new RewriteFilter());
    rwFilter.setDispatcherTypes(EnumSet.of(DispatcherType.FORWARD, DispatcherType.REQUEST,
    DispatcherType.ASYNC, DispatcherType.ERROR));
    rwFilter.addUrlPatterns("/*");
    return rwFilter;
    }
}
