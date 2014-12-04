package be.rubus.web.jerry.config.logging;

import be.rubus.web.jerry.provider.BeanProvider;
import be.rubus.web.jerry.startup.StartupEvent;
import be.rubus.web.jerry.utils.ProxyUtils;

import javax.enterprise.event.Observes;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class StartupLogging {

    protected String separator = System.getProperty("line.separator");

    public void logAtStartApplication(@Observes StartupEvent event) {
        List<ModuleConfig> configs = BeanProvider.getContextualReferences(ModuleConfig.class, false);
        StringBuilder configInfo = new StringBuilder();
        for (ModuleConfig config : configs) {
            configInfo.append(getConfigInfo(config));
        }
        System.out.println(configInfo.toString());
    }

    //generic alternative to #toString to avoid an overriden #toString at custom implementations
    protected String getConfigInfo(ModuleConfig config) {
        StringBuilder info = new StringBuilder();

        List<String> methodNames = new ArrayList<>();

        Class currentClass = ProxyUtils.getUnproxiedClass(config.getClass());
        while (currentClass != null &&
                !Object.class.getName().equals(currentClass.getName())) {

            info.append("config implementation: ");
            info.append(currentClass.getName());
            info.append(separator);

            //inspect the other methods of the implementing class
            for (Method currentMethod : currentClass.getDeclaredMethods()) {
                if (!currentMethod.isAnnotationPresent(ConfigEntry.class) ||
                        methodNames.contains(currentMethod.getName())) {
                    continue;
                }

                methodNames.add(currentMethod.getName());

                info.append("   method:\t").append(currentMethod.getName());
                info.append(separator);

                addConfigEntryValue(config, info, currentMethod);

                info.append(separator);
                info.append(separator);
            }

            currentClass = currentClass.getSuperclass();
        }

        return info.toString();
    }

    private void addConfigEntryValue(ModuleConfig config, StringBuilder info, Method currentMethod) {
        ConfigEntry configEntry = currentMethod.getAnnotation(ConfigEntry.class);
        if (!Void.class.equals(configEntry.classResult())) {
            info.append("   value:\t").append(configEntry.classResult().getCanonicalName());
        } else {
            if (configEntry.value() != null && configEntry.value().length > 0) {

                info.append("   value:\t").append(Arrays.toString(configEntry.value()));
            } else {
                // TODO Verify it is a no-arg method
                Object value;
                try {
                    value = currentMethod.invoke(config);
                    info.append("   value:\t").append(value.toString());
                } catch (IllegalAccessException e) {
                    info.append("   value:\t[unknown]");
                } catch (InvocationTargetException e) {
                    info.append("   value: [unknown]");
                }
            }
        }


    }
}
