/*
 * Copyright 2014-2018 Rudy De Busscher
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
package be.atbash.ee.jsf.valerie.el;

import be.atbash.util.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
public class RecordingELResolver extends ELResolver {
    protected final Logger logger = LoggerFactory.getLogger(RecordingELResolver.class);

    private ELResolver wrapped;

    private boolean projectStageDevelopment;

    private Object baseObject;
    private String property;
    //forms the id for cross-validation within complex components
    private String expression;
    private boolean isPathRecordingStopped = false;

    public RecordingELResolver(ELResolver elResolver, boolean projectStageDevelopment) {
        this.wrapped = elResolver;
        this.projectStageDevelopment = projectStageDevelopment;
    }

    public Object getBaseObject() {
        return baseObject;
    }

    public String getProperty() {
        return property;
    }

    public String getPath() {
        if (logger.isTraceEnabled()) {
            logger.trace("extracted path: " + this.expression);
        }
        return this.expression;
    }

    public void reset() {
        this.baseObject = null;
        this.property = null;
        this.expression = null;
    }

    /**
     * path recording is performed to get a key for cross-validation<br/>
     * every base after the first call which is null stops recording<br/>
     * with a dynamic map syntax the last property in the middle of an expression has to be a string.
     * such a string result continues the path recording at the next call for the current expression.
     * <p/>
     * example: #{bean[bean.propertyNameProvider['nameOfProperty1']]['dynBean'].property}
     * <p/>
     * limitation for cross-validation: nameOfProperty1 has to be of type string.
     * other key types aren't supported yet
     *
     * @param elContext wrapped el-context
     * @param base      current base
     * @param property  property to resolve
     * @return result of the delegated method call
     */
    public Object getValue(ELContext elContext, Object base, Object property) {
        Object result = this.wrapped.getValue(elContext, base, property);

        //very first call for an expression
        if (this.expression == null) {
            this.expression = (String) property;
        }
        //#{bean[dynBase.propertyName]} -> base of dynBase is null -> stop path recording
        else if (base == null) {
            this.isPathRecordingStopped = true;
        } else {
            boolean propertyExists = false;
            String propertyName = property.toString();
            propertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

            try {
                if (base instanceof Map) {
                    if (((Map) base).containsKey(property)) {
                        propertyExists = true;
                    }
                } else if (ProxyUtils.getUnproxiedClass(base.getClass()).getMethod("get" + propertyName) != null) {
                    propertyExists = true;
                }
            } catch (NoSuchMethodException e) {
                try {
                    if (ProxyUtils.getUnproxiedClass(base.getClass()).getMethod("is" + propertyName) != null) {
                        propertyExists = true;
                    }
                } catch (NoSuchMethodException e1) {
                    if (logger.isTraceEnabled()) {
                        logger.trace("property: " + property +
                                " isn't used for path - it isn't a property of " + base.getClass());
                    }
                }
            }

            //e.g.: #{bean.subBase.property} -> here we are at subBase
            if (propertyExists && !this.isPathRecordingStopped) {
                this.expression += "." + property;
            } else if (propertyExists && result instanceof String) {
                this.isPathRecordingStopped = false;
            }
        }

        /*
        if(this.isPathRecordingStopped && result instanceof String)
        {
            this.expression += "." + property;
        }
        */

        return result;
    }

    public Class<?> getType(ELContext elContext, Object o, Object o1) {
        return wrapped.getType(elContext, o, o1);
    }

    public void setValue(ELContext elContext, Object o, Object o1, Object o2) {
        if (o1 instanceof String) {
            if (expression == null) {
                expression = (String) o1;
            } else {
                expression += "." + o1;
            }
            property = (String) o1;
        } else {
            logWarningForUnsupportedExpression(o1);
        }

        baseObject = o;
        elContext.setPropertyResolved(true);
    }

    public boolean isReadOnly(ELContext elContext, Object o, Object o1) {
        return wrapped.isReadOnly(elContext, o, o1);
    }

    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext elContext, Object o) {
        return wrapped.getFeatureDescriptors(elContext, o);
    }

    public Class<?> getCommonPropertyType(ELContext elContext, Object o) {
        return wrapped.getCommonPropertyType(elContext, o);
    }

    public static ELContext createContextWrapper(final ELContext context, final ELResolver resolver) {
        return new ELContext() {
            @Override
            public Locale getLocale() {
                return context.getLocale();
            }

            @Override
            public void setPropertyResolved(boolean value) {
                super.setPropertyResolved(value);
                context.setPropertyResolved(value);
            }

            @Override
            public void putContext(Class clazz, Object object) {
                super.putContext(clazz, object);
                context.putContext(clazz, object);
            }

            @Override
            public Object getContext(Class clazz) {
                return context.getContext(clazz);
            }

            @Override
            public void setLocale(Locale locale) {
                super.setLocale(locale);
                context.setLocale(locale);
            }

            @Override
            public ELResolver getELResolver() {
                return resolver;
            }

            @Override
            public FunctionMapper getFunctionMapper() {
                return context.getFunctionMapper();
            }

            @Override
            public VariableMapper getVariableMapper() {
                return context.getVariableMapper();
            }

        };
    }

    private void logWarningForUnsupportedExpression(Object o1) {
        if (this.projectStageDevelopment) {
            try {
                logger.warn(o1 + " is not a valid property for constraint based validation.");
            } catch (NullPointerException e) {
                logger.warn("A property which doesn't support constraint based validation has been detected.");
            }
        }
    }
}
