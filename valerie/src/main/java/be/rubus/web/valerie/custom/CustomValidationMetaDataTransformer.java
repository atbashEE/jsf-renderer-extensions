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
 *
 */
package be.rubus.web.valerie.custom;

import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.MetaDataTransformer;
import be.rubus.web.valerie.validation.BeanValidationMetaDataFilter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class CustomValidationMetaDataTransformer implements MetaDataTransformer {

    @Inject
    private BeanValidationMetaDataFilter beanValidationMetaDataFilter;

    @Override
    public Map<String, Object> convertMetaData(MetaDataEntry metaData) {
        Map<String, Object> result = new HashMap<>();

        if (beanValidationMetaDataFilter.isBeanValidationConstraint(metaData)) {
            convert(metaData, result);
        }
        return result;

    }

    private void convert(MetaDataEntry metaData, Map<String, Object> result) {
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
    }
}
