/*
 * Copyright 2014-2022 Rudy De Busscher
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
package be.atbash.ee.jsf.valerie.metadata;

import be.atbash.ee.jsf.jerry.metadata.CommonMetaDataKeys;
import be.atbash.ee.jsf.jerry.metadata.MetaDataEntry;
import be.atbash.ee.jsf.jerry.metadata.MetaDataTransformer;
import be.atbash.ee.jsf.valerie.validation.BeanValidationMetaDataFilter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * MetaDataTransformer for BeanValidation annotations. Picked up by CDI.
 */
@ApplicationScoped
public class BeanValidationMetaDataTransformer implements MetaDataTransformer {

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
        if (NotNull.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
        }
        if (Size.class.getName().equals(metaData.getKey())) {
            Size size = (Size) metaData.getValue();
            if (size.max() < Integer.MAX_VALUE) {
                result.put(CommonMetaDataKeys.SIZE.getKey(), size.max());
            }
        }
        if (Past.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.PAST.getKey(), Boolean.TRUE);
        }
        if (Future.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.FUTURE.getKey(), Boolean.TRUE);
        }
    }
}
