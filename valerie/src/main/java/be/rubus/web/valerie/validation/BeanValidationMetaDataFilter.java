package be.rubus.web.valerie.validation;

import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.valerie.storage.MetaDataEntryFilter;
import be.rubus.web.valerie.utils.AnnotationUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 * Test if the MetaDataEntry is for a  Bean Validation constraint.
 */
@ApplicationScoped
public class BeanValidationMetaDataFilter implements MetaDataEntryFilter {

    private Map<String, Boolean> cache = new HashMap<>();

    @Override
    public boolean test(MetaDataEntry entry) {
        boolean result;
        if (cache.containsKey(entry.getKey())) {
            result = cache.get(entry.getKey());
        } else {
            result = AnnotationUtils.isBeanValidationConstraint(entry.getKey());
            cache.put(entry.getKey(), result);
        }
        return result;
    }
}
