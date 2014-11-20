package be.rubus.web.valerie.utils;

import be.rubus.web.jerry.provider.BeanProvider;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.valerie.property.DefaultPropertyInformation;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.property.PropertyInformationKeys;
import be.rubus.web.valerie.storage.PropertyStorage;

import javax.validation.Constraint;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class AnnotationUtils {

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

            //LOGGER.finest(annotation.getClass().getName() + " found");
        }
    }

    private static MetaDataEntry createMetaDataEntryForAnnotation(Annotation foundAnnotation) {
        MetaDataEntry entry = new MetaDataEntry();

        entry.setKey(foundAnnotation.annotationType().getName());
        entry.setValue(foundAnnotation);

        return entry;
    }

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

    public static boolean isBeanValidationConstraint(String annotationName) {
        boolean result;
        try {
            Class<?> annotationClass = Class.forName(annotationName);
            result = annotationClass.getAnnotation(Constraint.class) != null;
        } catch (ClassNotFoundException e) {
            result = false;
        }
        return result;

    }
}
