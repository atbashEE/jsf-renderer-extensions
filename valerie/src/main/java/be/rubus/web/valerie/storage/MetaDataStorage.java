package be.rubus.web.valerie.storage;

import be.rubus.web.jerry.provider.BeanProvider;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.valerie.property.DefaultPropertyInformation;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.property.PropertyInformationKeys;
import be.rubus.web.valerie.utils.ProxyUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
@ApplicationScoped
// FIXME This can be refactored so that it uses the ComponentStorage.
public class MetaDataStorage {

    //protected final Logger logger = Logger.getLogger(getClass().getName());

    private Map<String, Map<String, PropertyInformation>> cachedPropertyInformation =
            new ConcurrentHashMap<>();

    private List<MetaDataEntryFilter> metaDataEntryFilters;

    @PostConstruct
    private void initFilters() {

        metaDataEntryFilters = BeanProvider.getContextualReferences(MetaDataEntryFilter.class, true, false);

    }

    public void storeMetaDataOf(PropertyInformation propertyInformation) {

        PropertyInformation propertyInformationToStore = new DefaultPropertyInformation();

        PropertyDetails propertyDetails = propertyInformation
                .getInformation(PropertyInformationKeys.PROPERTY_DETAILS, PropertyDetails.class);

        copyMetaData(propertyInformation, propertyInformationToStore);

        getMapForClass(ProxyUtils.getUnproxiedClass(propertyDetails.getBaseObject().getClass()))
                .put(propertyDetails.getProperty(), propertyInformationToStore);
    }


    public MetaDataEntry[] getMetaData(Class targetClass, String targetProperty) {
        PropertyInformation propertyInformation = getMapForClass(targetClass).get(targetProperty);

        PropertyInformation clonedPropertyInformation = new DefaultPropertyInformation();
        copyMetaData(propertyInformation, clonedPropertyInformation);

        return clonedPropertyInformation.getMetaDataEntries();
    }

    public boolean containsMetaDataFor(Class targetClass, String targetProperty) {
        return getMapForClass(targetClass).containsKey(targetProperty);
    }

    private void copyMetaData(PropertyInformation source, PropertyInformation target) {
        MetaDataEntry newMetaDataEntry;
        for (MetaDataEntry metaDataEntry : source.getMetaDataEntries()) {
            if (metaDataEntryRetained(metaDataEntry)) {

                target.addMetaDataEntry(metaDataEntry);
            }
        }
    }

    private boolean metaDataEntryRetained(MetaDataEntry metaDataEntry) {
        boolean result = false;
        Iterator<MetaDataEntryFilter> iterator = metaDataEntryFilters.iterator();
        while (!result && iterator.hasNext()) {
            MetaDataEntryFilter filter = iterator.next();
            result = filter.test(metaDataEntry);
        }
        return result;
    }

    private Map<String, PropertyInformation> getMapForClass(Class target) {
        String key = ProxyUtils.getClassName(target);
        if (!this.cachedPropertyInformation.containsKey(key)) {
            this.cachedPropertyInformation.put(key,
                    //new NullValueAwareConcurrentHashMap<String, PropertyInformation>(new NullMarkerPropertyInformation()));
                    new HashMap<String, PropertyInformation>());
        }
        return this.cachedPropertyInformation.get(key);
    }

    private Class<? extends MetaDataEntryFilter> getStorageFilterClass(MetaDataEntryFilter storageFilter) {
        return ProxyUtils.getUnproxiedClass(storageFilter.getClass(), MetaDataEntryFilter.class);
    }
}