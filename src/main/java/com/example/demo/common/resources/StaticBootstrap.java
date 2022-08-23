package com.example.demo.common.resources;

import com.example.demo.common.L;
import com.example.demo.common.util.ProcessUtils;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StaticBootstrap {

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("cn.maidao.hanlin"));
        {
            java.util.Set<Class<?>> staticInitClasses = reflections.getTypesAnnotatedWith(StaticInit.class);
            for (Class<?> clazz : staticInitClasses) {
                try {
                    if (L.isInfoEnabled()) {
                        L.info("[StaticBootstrap] calling: " + clazz.getCanonicalName());
                    }
                    Class.forName(clazz.getCanonicalName());
                } catch (Throwable t) {
                    ProcessUtils.exitWithMessage(null, t);
                }
            }
        }
        if (L.isInfoEnabled()) {
            L.info("[StaticBootstrap] done");
        }
    }

}
