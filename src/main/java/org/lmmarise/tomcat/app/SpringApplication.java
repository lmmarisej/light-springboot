package org.lmmarise.tomcat.app;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * Spring应用程序引导类
 *
 * @author lmmarise.j@gmail.com
 * @since 2021/9/6 12:28 下午
 */
public class SpringApplication {

    public static void run() {
        // 创建tomcat实例
        Tomcat tomcat = new Tomcat();
        // 设置tomcat端口
        tomcat.setPort(8080);
        try {
            //此处随便指定一下webapp，让tomcat知道这是一个web工程
            tomcat.addWebapp("/", "/");
            //启动tomcat
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

}
