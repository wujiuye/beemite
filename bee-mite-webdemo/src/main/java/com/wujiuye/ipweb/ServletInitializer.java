package com.wujiuye.ipweb;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;



public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * SpringBootServletInitializer重写了WebApplicationInitializer的onStartup方法，
     * 并在onStartup方法中完成spring容器的初始化，
     * 初始化流程：
     *      SpringBootServletInitializer::onStartup
     *          ->createRootApplicationContext
     *          ->run
     *   -> SpringApplication::run
     *          ->createApplicationContext
     * 所以说也还是调用SpringApplication的run方法来启动的
     * 最终容易的实例化是在SpringApplication的createApplicationContext方法中，默认是使用AnnotationConfigApplicationContext
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        /**
         * 需要指定源配置类，即我们可以重写创建一个类
         * 然后使用@SpringBootApplication注解，application.sources(被@SpringBootApplication注解的类);
         * 例如：
         *    application.sources(Weiai7lvApplicationConfig.class);
         * 只会处理sources类上面的注解和@Bean的方法
         */
        return application.sources(DemoAppcliction.class);
    }


    /**
     * 重写onStartup方法代替web.xml做一些配置
     * @param servletContext
     * @throws ServletException
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //必须调用父类的onStartup方法
        super.onStartup(servletContext);
    }
}
