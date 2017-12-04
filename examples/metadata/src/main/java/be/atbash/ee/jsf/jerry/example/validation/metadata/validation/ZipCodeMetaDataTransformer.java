/*
 * Copyright 2014-2017 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.example.validation.metadata.validation;

import be.atbash.ee.jsf.jerry.metadata.CommonMetaDataKeys;
import be.atbash.ee.jsf.jerry.metadata.MetaDataEntry;
import be.atbash.ee.jsf.jerry.metadata.MetaDataTransformer;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class ZipCodeMetaDataTransformer implements MetaDataTransformer {
    @Override
    public Map<String, Object> convertMetaData(MetaDataEntry metaData) {
        Map<String, Object> result = new HashMap<>();
        if (ZipCode.class.getName().equals(metaData.getKey())) {
            result.put(CommonMetaDataKeys.REQUIRED.getKey(), Boolean.TRUE);
            result.put(CommonMetaDataKeys.SIZE.getKey(), 4);
            result.put(ZipCode.class.getName(), Boolean.TRUE);
        }
        return result;
    }
}
