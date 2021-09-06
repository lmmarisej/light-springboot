package org.lmmarise.tomcat.controller;

import org.lmmarise.tomcat.web.SpringBootConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lmmarise.j@gmail.com
 * @since 2021/9/6 12:43 下午
 */
@RestController
public class TestController implements InitializingBean, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("http://localhost:8080/app/test");
    }

    @RequestMapping("/app/test")
    public String test() {
        return applicationContext.getBean(SpringBootConfig.A.class).toString();
    }

}
