package com.github.cylyl.springdrop;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.lifecycle.ServerLifecycleListener;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.util.component.LifeCycle;
import org.glassfish.jersey.internal.inject.InjectionResolver;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Iterator;
import java.util.Map;

/**
 * Ref: https://blog.adamgamboa.dev/integrating-spring-framework-in-dropwizard/
 */
public class SpringDrop<T extends Configuration>  {

    private ConfigurableApplicationContext context;
    private DispatcherServlet dispatchServlet;
    public static final String ENVIRONMENT_BEAN_NAME = "sdEnv";


    public SpringDrop() {
        this.context = new AnnotationConfigWebApplicationContext();
    }

    public SpringDrop(String... basePackages) {
        dispatchServlet = new DispatcherServlet(new GenericWebApplicationContext());
        context = new AnnotationConfigWebApplicationContext();
        if( basePackages != null ){
            ((AnnotationConfigWebApplicationContext) context).scan(basePackages);
        }
    }

    public SpringDrop(ConfigurableApplicationContext configurableApplicationContext) {
        this.context = configurableApplicationContext;
    }

    public void initial(io.dropwizard.Configuration c, Environment e, String coonfigLocation, String... basePackages ) {

        AnnotationConfigWebApplicationContext context = (AnnotationConfigWebApplicationContext) this.context;
        context.setConfigLocation(coonfigLocation);
        context.scan(basePackages);
        e.servlets().addServletListeners((new ContextLoaderListener(context)));
        ServletRegistration.Dynamic dispatcher = e.servlets().addServlet("SpringDispatcherServlet", new DispatcherServlet(context){
            @Override
            public void init(ServletConfig config) throws ServletException {
                super.init(config);
                try {
                    initial(c,e);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/sd/*");
        LOG.info("Default Spring mapping contect is : \"/sd/*\"");
    }

    private void initial(Object configuration, Environment environment) throws Exception {

        if (!this.context.isActive()) {
            this.context.refresh();
        }
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton("dwConfig", configuration);
        LOG.info("Registering Dropwizard Configuration under name : dwConfig");

        beanFactory.registerSingleton("dwEnv", environment);
        LOG.info("Registering Dropwizard Environment under name : dwEnv");

        Map<String, Managed> managedMap = context.getBeansOfType(Managed.class);
        Iterator iterator = managedMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            Managed managed = (Managed)managedMap.get(beanName);
            environment.lifecycle().manage(managed);
            LOG.info("Registering managed: " + managed.getClass().getName());
        }

        Map<String, LifeCycle> lifeCycleMap = context.getBeansOfType(LifeCycle.class);
        iterator = lifeCycleMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            if (!beanName.equals(ENVIRONMENT_BEAN_NAME)) {
                LifeCycle lifeCycle = (LifeCycle)lifeCycleMap.get(beanName);
                environment.lifecycle().manage(lifeCycle);
                LOG.info("Registering lifeCycle: " + lifeCycle.getClass().getName());
            }
        }

        Map<String, ServerLifecycleListener> serverLifecycleListenerMap = context.getBeansOfType(ServerLifecycleListener.class);
        iterator = serverLifecycleListenerMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            if (!beanName.equals(ENVIRONMENT_BEAN_NAME)) {
                ServerLifecycleListener serverLifecycleListener = (ServerLifecycleListener)serverLifecycleListenerMap.get(beanName);
                environment.lifecycle().addServerLifecycleListener(serverLifecycleListener);
                LOG.info("Registering serverLifecycleListener: " + serverLifecycleListener.getClass().getName());
            }
        }

        Map<String, Task> taskMap = context.getBeansOfType(Task.class);
        iterator = taskMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            Task task = (Task)taskMap.get(beanName);
            environment.admin().addTask(task);
            LOG.info("Registering task: " + task.getClass().getName());
        }

        Map<String, HealthCheck> healthCheckMap = context.getBeansOfType(HealthCheck.class);
        iterator = healthCheckMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            HealthCheck healthCheck = (HealthCheck)healthCheckMap.get(beanName);
            environment.healthChecks().register(beanName, healthCheck);
            LOG.info("Registering healthCheck: " + healthCheck.getClass().getName());
        }


        Map<String, InjectionResolver> injectionResolverMap = context.getBeansOfType(InjectionResolver.class);
        iterator = injectionResolverMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            InjectionResolver injectableProvider = (InjectionResolver)injectionResolverMap.get(beanName);
            environment.jersey().register(injectableProvider);
            LOG.info("Registering injectable provider: " + injectableProvider.getClass().getName());
        }

        Map<String, Object> objectMap = context.getBeansWithAnnotation(Provider.class);
        iterator = objectMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            Object provider = objectMap.get(beanName);
            environment.jersey().register(provider);
            LOG.info("Registering provider : " + provider.getClass().getName());
        }

        Map<String, Object> pathMap = context.getBeansWithAnnotation(Path.class);
        iterator = pathMap.keySet().iterator();

        while(iterator.hasNext()) {
            String beanName = (String)iterator.next();
            Object resource = pathMap.get(beanName);
            environment.jersey().register(resource);
            LOG.info("Registering resource : " + resource.getClass().getName());
        }
    }
}
