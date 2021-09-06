package org.lmmarise.tomcat.web;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.SpringServletContainerInitializer;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * {@link HandlesTypes} 指定 {@link MySpringServletContainerInitializer} 可以处理的类型为 {@link MyWebApplicationInitializer}
 *
 * 由于依赖了 spring-web-5.3.9.jar 包，所以有 {@link SpringServletContainerInitializer} ，因此当前类不用也行：需要注释SPI文件中指定的实现
 *
 * @author lmmarise.j@gmail.com
 * @since 2021/9/6 12:34 下午
 */
@HandlesTypes(WebApplicationInitializer.class)
public class MySpringServletContainerInitializer implements ServletContainerInitializer {

    /**
     * {@link HandlesTypes} 是javax包下的，实现它的是Servlet容器。
     * {@link HandlesTypes }中value指的是“可以处理的类型”，这个值是给Servlet容器用的。
     * 举例，Tomcat对@HandlesTypes的实现是，通过BCEL字节码框架扫描/WEB-INF/classes和jar，根据.class的基类和接口名匹配@HandlesTypes
     * 的value类名（ContextConfig#checkHandlesTypes），反射加载匹配上的Class为Set，将Set传入ServletContainerInitializer#onStartup的第一个参数，
     * 最终由ServletContainerInitializer利用反射创建实例并调用接口方法。
     */
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {
        List<WebApplicationInitializer> initializers = new LinkedList<>();

        if (webAppInitializerClasses != null) {
            for (Class<?> waiClass : webAppInitializerClasses) {
                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
                        WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
                    try {
                        initializers.add((WebApplicationInitializer)
                                ReflectionUtils.accessibleConstructor(waiClass).newInstance());
                    } catch (Throwable ex) {
                        throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
                    }
                }
            }
        }

        if (initializers.isEmpty()) {
            servletContext.log("No Spring WebApplicationInitializer types detected on classpath");
            return;
        }

        servletContext.log(initializers.size() + " Spring WebApplicationInitializers detected on classpath");
        AnnotationAwareOrderComparator.sort(initializers);
        for (WebApplicationInitializer initializer : initializers) {
            initializer.onStartup(servletContext);
        }
    }
}