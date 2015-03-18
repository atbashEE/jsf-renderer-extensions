package be.rubus.web.valerie.recording;

import be.rubus.web.jerry.metadata.MetaDataEnhancer;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.MetaDataHolder;
import be.rubus.web.jerry.metadata.PropertyInformationKeys;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.utils.AnnotationUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 *
 */
@ApplicationScoped
public class RecordingInfoEnhancer implements MetaDataEnhancer {

    @Override
    public void enhanceData(MetaDataHolder holder) {
        for (MetaDataEntry metaDataEntry : holder.getMetaDataEntries()) {
            if (RecordValue.class.getName().equals(metaDataEntry.getKey())) {
                enhanceData(metaDataEntry);
            }
        }
    }

    private void enhanceData(MetaDataEntry metaDataEntry) {
        RecordValue recordValue = (RecordValue) metaDataEntry.getValue();
        if (recordValue.value().length == 0) {
            PropertyDetails details = metaDataEntry.getProperty(PropertyInformationKeys.PROPERTY_DETAILS, PropertyDetails.class);
            List<RecordValueInfo> classLevelBeanValidationInfo = AnnotationUtils.getClassLevelBeanValidationInfo(details.getBaseObject().getClass());

            for (RecordValueInfo recordValueInfo : classLevelBeanValidationInfo) {

                recordValueInfo.setClassProperty(details.getProperty());
            }
            metaDataEntry.setProperty(PropertyInformationKeys.RECORDING_INFORMATION, classLevelBeanValidationInfo);
        } else {
            // FIXME class level specified on annotation
        }
    }
}
