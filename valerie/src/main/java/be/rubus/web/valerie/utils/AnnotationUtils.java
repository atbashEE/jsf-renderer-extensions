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
package be.rubus.web.valerie.utils;

import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.PropertyInformationKeys;
import be.rubus.web.jerry.producer.LogProducer;
import be.rubus.web.jerry.provider.BeanProvider;
import be.rubus.web.valerie.property.DefaultPropertyInformation;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.recording.RecordValueInfo;
import be.rubus.web.valerie.storage.PropertyStorage;
import org.slf4j.Logger;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class AnnotationUtils {

    private static final Logger LOGGER = LogProducer.getLogger(AnnotationUtils.class);

    private static List<Class<?>> excludedAnnotations = new ArrayList<>();

    static {
        excludedAnnotations.add(Documented.class);
        excludedAnnotations.add(Retention.class);
        excludedAnnotations.add(Target.class);
    }

    private AnnotationUtils() {
    }

    /**
     * Extracts all annotations found on a property. It looks for them on getter method, the field and all getters
     * that are defined in interfaces. The name of the target property is provided by the propertyDetails parameter.
     *
     * @param entityClass     target class which has to be scanned
     * @param propertyDetails information about the property
     * @return a datastructure which contains all information about the target-property
     */
    public static PropertyInformation extractAnnotations(Class entityClass, PropertyDetails propertyDetails) {
        PropertyInformation propertyInformation = new DefaultPropertyInformation();
        propertyInformation.setInformation(PropertyInformationKeys.PROPERTY_DETAILS, propertyDetails);

        PropertyStorage storage = BeanProvider.getContextualReference(PropertyStorage.class);

        while (!Object.class.getName().equals(entityClass.getName())) {
            addPropertyAccessAnnotations(storage, entityClass, propertyDetails.getProperty(), propertyInformation);
            addFieldAccessAnnotations(storage, entityClass, propertyDetails.getProperty(), propertyInformation);

            processInterfaces(storage, entityClass, propertyDetails, propertyInformation);

            entityClass = entityClass.getSuperclass();
        }

        return propertyInformation;
    }

    /**
     * Extracts all annotations found at the getter method of a property.
     * The annotations are added to the given propertyInformation parameter.
     *
     * @param storage             {@link PropertyStorage} which is able to cache information of a property
     * @param entity              target class which has to be scanned
     * @param property            Name of the property we are interested in.
     * @param propertyInformation Where the MetaDataEntries for the annotations are added.
     */
    public static void addPropertyAccessAnnotations(PropertyStorage storage,
                                                    Class entity,
                                                    String property,
                                                    PropertyInformation propertyInformation) {
        Method method = ReflectionUtils.tryToGetMethodOfProperty(storage, entity, property);

        if (method != null) {
            addAnnotationToAnnotationEntries(Arrays.asList(method.getAnnotations()), propertyInformation);
        }
    }

    /**
     * Extracts all annotations found at the field of the property.
     * A field name with a _ (underscore) as prefix is also supported.
     * The annotations are added to the given propertyInformation parameter.
     *
     * @param storage             {@link PropertyStorage} which is able to cache information of a property
     * @param entity              target class which has to be scanned
     * @param property            Name of the property we are interested in.
     * @param propertyInformation Where the MetaDataEntries for the annotations are added.
     */
    public static void addFieldAccessAnnotations(PropertyStorage storage,
                                                 Class entity,
                                                 String property,
                                                 PropertyInformation propertyInformation) {
        Field field = ReflectionUtils.tryToGetFieldOfProperty(storage, entity, property);

        if (field != null) {
            addAnnotationToAnnotationEntries(Arrays.asList(field.getAnnotations()), propertyInformation);
        }
    }

    private static void processInterfaces(PropertyStorage storage, Class currentClass,
                                          PropertyDetails propertyDetails,
                                          PropertyInformation propertyInformation) {
        for (Class currentInterface : currentClass.getInterfaces()) {
            addPropertyAccessAnnotations(storage, currentInterface, propertyDetails.getProperty(), propertyInformation);

            processInterfaces(storage, currentInterface, propertyDetails, propertyInformation);
        }
    }

    private static void addAnnotationToAnnotationEntries(List<Annotation> annotations,
                                                         PropertyInformation propertyInformation) {
        for (Annotation annotation : annotations) {
            propertyInformation.addMetaDataEntry(createMetaDataEntryForAnnotation(annotation));

            addCombinedConstraints(propertyInformation, annotation);

            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(annotation.getClass().getName() + " found");
            }
        }
    }

    private static void addCombinedConstraints(PropertyInformation propertyInformation, Annotation annotation) {
        Annotation[] declaredAnnotations = annotation.annotationType().getDeclaredAnnotations();
        List<Annotation> annotations = new ArrayList<>();

        for (Annotation foundAnnotation : declaredAnnotations) {
            if (!excludedAnnotations.contains(foundAnnotation.annotationType())) {
                annotations.add(foundAnnotation);
            }
        }

        addAnnotationToAnnotationEntries(annotations, propertyInformation);
    }

    private static MetaDataEntry createMetaDataEntryForAnnotation(Annotation foundAnnotation) {
        MetaDataEntry entry = new MetaDataEntry();

        entry.setKey(foundAnnotation.annotationType().getName());
        entry.setValue(foundAnnotation);

        return entry;
    }

    // TODO Remove as it is not used

    /**
     * Extracts the value of the given annotation.
     *
     * @param annotation  The target annotation
     * @param targetClass Type of the value-property
     * @param <T>         Result class
     * @return value of the value-property
     */
    public static <T> T extractValueOf(Annotation annotation, Class<T> targetClass) {
        // Since we can't be sure of the type, we can't use the
        // annotation.annotationType().getDeclaredMethod(String, Class...) method.
        for (Method annotationMethod : annotation.annotationType().getDeclaredMethods()) {
            if ("value".equals(annotationMethod.getName())) {
                try {
                    return (T) annotationMethod.invoke(annotation);
                } catch (Exception e) {
                    //do nothing
                }
            }
        }
        return null;
    }

    public static boolean isBeanConstraint(String annotationName) {
        boolean result = false;
        try {
            Class<?> annotationClass = Class.forName(annotationName);
            result = annotationClass.getAnnotation(Constraint.class) != null;

        } catch (ClassNotFoundException e) {
            ; // TODO At least log this, probably some class loading issue that we have to solve
        }
        return result;

    }

    public static List<Class<? extends ConstraintValidator<?, ?>>> getBeanConstraintValidator(String annotationName) {
        List<Class<? extends ConstraintValidator<?, ?>>> result = new ArrayList<>();
        try {
            Class<?> annotationClass = Class.forName(annotationName);
            result = getBeanConstraintValidator(annotationClass);
        } catch (ClassNotFoundException e) {
            ; // TODO At least log this, probably some class loading issue that we have to solve
        }
        return result;

    }

    private static List<Class<? extends ConstraintValidator<?, ?>>> getBeanConstraintValidator(Class annotationClass) {
        Constraint annotation = (Constraint) annotationClass.getAnnotation(Constraint.class);

        List<Class<? extends ConstraintValidator<?, ?>>> result = new ArrayList<>();
        if (annotation != null) {
            result = Arrays.asList(annotation.validatedBy());
        }

        return result;

    }

    public static List<RecordValueInfo> getClassLevelBeanValidationInfo(Class clazz) {
        List<RecordValueInfo> result = new ArrayList<>();

        for (Annotation annotation : clazz.getDeclaredAnnotations()) {
            List<Class<? extends ConstraintValidator<?, ?>>> beanValidationConstraints = getBeanConstraintValidator(annotation.annotationType());
            for (Class<? extends ConstraintValidator<?, ?>> validationConstraint : beanValidationConstraints) {
                result.add(new RecordValueInfo(clazz, validationConstraint));
            }
        }

        if (!Object.class.equals(clazz.getSuperclass())) {
            result.addAll(getClassLevelBeanValidationInfo(clazz.getSuperclass()));
        }
        return result;
    }
}
