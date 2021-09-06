package org.lmmarise.tomcat.web;

import org.springframework.context.annotation.*;

import javax.servlet.http.HttpServlet;

/**
 * @author lmmarise.j@gmail.com
 * @since 2021/9/6 12:28 下午
 */
@Configuration(proxyBeanMethods = false)    // 开启后 b() 方法的调用将被代理
@ComponentScan("org.lmmarise.tomcat")
public class SpringBootConfig extends HttpServlet {

    public static class A {
        @Override
        public String toString() {
            return "a";
        }
    }

    static class B {
    }

    @Bean
    public B b() {
        System.out.println("new B");
        return new B();
    }

    @Bean
    @Scope("prototype")
    public A string() {
        System.out.println("new A");
        b();
        return new A();
    }

}