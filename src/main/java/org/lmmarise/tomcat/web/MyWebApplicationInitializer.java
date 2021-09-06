package org.lmmarise.tomcat.web;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * 实现web.xml的配置
 *
 * @author lmmarise.j@gmail.com
 * @since 2021/9/6 12:30 下午
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    static int a = -1;

    /**
     * 传入servletContext，创建Spring应用上下文容器 & DispatcherServlet，放入servletContext
     */
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (a == 0) return;     // 避免 @HandlesTypes(WebApplicationInitializer.class) 使用接口扫描到多个符合条件的 WebApplicationInitializer，
        // 导致被 MySpringServletContainerInitializer 多次调用
        ++a;

        System.out.println("初始化 MyWebApplicationInitializer");
        // 注解驱动SpringMVC应用上下文，扫描Classpath下的SpringMVC注解
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        // 设置容器的SpringBean配置类
        ac.register(SpringBootConfig.class);
        ac.refresh();

        // 创建DispatcherServlet，注册到servletContext，以处理tomcat接收到的请求
        DispatcherServlet servlet = new DispatcherServlet(ac);
        // web.xml替代品
        ServletRegistration.Dynamic registration = servletContext.addServlet("/", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
    }

}