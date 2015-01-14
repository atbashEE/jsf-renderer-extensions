/*
 * Copyright 2014-2015 Rudy De Busscher
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
package be.rubus.web.valerie.metadata;

import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.MetaDataTransformer;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
        if (Size.class.getName().equals(metaData.getKey())) {
            Size size = (Size) metaData.getValue();
            if (size.min() > 0) {
                result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
            }
            if (size.max() < 2147483647) {
                result.put(CommonMetaDataKeys.SIZE.getKey(), size.max());
            }
        }
        return result;
    }
}
