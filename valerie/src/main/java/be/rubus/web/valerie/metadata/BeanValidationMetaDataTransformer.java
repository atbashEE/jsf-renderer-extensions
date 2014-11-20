package be.rubus.web.valerie.metadata;

import be.rubus.web.jerry.metadata.*;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class BeanValidationMetaDataTransformer implements MetaDataTransformer {
    @Override
    public Map<String, Object> convertMetaData(MetaDataEntry metaData) {
        Map<String, Object> result = new HashMap<>();
        if (NotNull.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
        }
        return result;
    }
}
