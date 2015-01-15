package be.rubus.web.valerie.custom;

import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.MetaDataTransformer;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class CustomValidationMetaDataTransformer implements MetaDataTransformer {
    @Override
    public Map<String, Object> convertMetaData(MetaDataEntry metaData) {
        Map<String, Object> result = new HashMap<>();

        if (ValSize.class.getName().equals(metaData.getKey())) {
            ValSize size = (ValSize) metaData.getValue();
            if (size.min() > 0) {
                result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
            }
            if (size.max() < Integer.MAX_VALUE) {
                result.put(CommonMetaDataKeys.SIZE.getKey(), size.max());
            }
        }
        if (ValPast.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.PAST.getKey(), Boolean.TRUE);
            result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
        }

        if (ValFuture.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.FUTURE.getKey(), Boolean.TRUE);
            result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
        }
        return result;

    }
}
