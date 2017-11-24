/*
 * Copyright 2014-2016 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jsf.jerry.config.logging;

import be.atbash.ee.jsf.jerry.config.DynamicConfigValueHelper;
import be.atbash.ee.jsf.jerry.provider.BeanProvider;
import be.atbash.ee.jsf.jerry.startup.StartupEvent;
import be.atbash.ee.jsf.jerry.utils.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *FIXME Moved to Atbash Configuration
 */
public class StartupLogging {

    private Logger logger = LoggerFactory.getLogger(StartupLogging.class);

    protected String separator = System.getProperty("line.separator");

    private boolean allLoggingActivated;

    @Inject
    private DynamicConfigValueHelper valueHelper;

    public void logAtStartApplication(@Observes StartupEvent event) {

        checkAllLogging();

        List<ModuleConfig> configs = BeanProvider.getContextualReferences(ModuleConfig.class, false);
        StringBuilder configInfo = new StringBuilder();
        configInfo.append('\n');
        for (ModuleConfig config : configs) {
            configInfo.append(getConfigInfo(config));
        }
        logger.info(configInfo.toString());
    }

    private void checkAllLogging() {
        String jerryLogAllProperty = System.getProperty("jerry.log.all");
        allLoggingActivated = "true".equalsIgnoreCase(jerryLogAllProperty);
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
                if (currentMethod.getParameterTypes().length == 0) {
                    if (void.class.equals(currentMethod.getReturnType())) {
                        info.append("   value:\tunknown - Method has no return value");
                    } else {

                        executeMethodForConfigRetrieval(config, info, currentMethod, configEntry.noLogging());
                    }
                } else {
                    info.append("   value:\tunknown - Method has a parameter");
                }
            }
        }

    }

    private void executeMethodForConfigRetrieval(ModuleConfig config, StringBuilder info, Method currentMethod, boolean noLogging) {
        Object value;
        try {
            value = currentMethod.invoke(config);
            if (noLogging && !allLoggingActivated) {
                info.append("   value:\t").append("No logging parameter active ").append(value == null ? "null" : "[non null value]");
            } else {
                info.append("   value:\t");
                if (value == null) {
                    info.append("null");
                } else {
                    info.append(valueHelper.getTruncatedConfigValue(value.toString()));
                }

            }
        } catch (IllegalAccessException e) {
            info.append("   value:\t[unknown]");
        } catch (InvocationTargetException e) {
            info.append("   value:\t [unknown]");
        }
    }
}
