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
package be.atbash.ee.jsf.valerie.recording;

import be.atbash.ee.jsf.jerry.metadata.MetaDataEnhancer;
import be.atbash.ee.jsf.jerry.metadata.MetaDataEntry;
import be.atbash.ee.jsf.jerry.metadata.MetaDataHolder;
import be.atbash.ee.jsf.jerry.metadata.PropertyInformationKeys;
import be.atbash.ee.jsf.valerie.property.PropertyDetails;
import be.atbash.ee.jsf.valerie.utils.AnnotationUtils;

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
