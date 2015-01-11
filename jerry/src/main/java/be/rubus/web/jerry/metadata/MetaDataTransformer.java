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
package be.rubus.web.jerry.metadata;

import java.util.Map;

/**
 * {@link be.rubus.web.jerry.metadata.MetaDataTransformer MetaDataTransformers}
 * are used to convert specific information of constraints to a generic representation.
 * So it's possible to transform different meta-data implementations to an independent representation.
 * E.g. @Length and @Size specifies the same information.
 * {@link be.rubus.web.jerry.metadata.MetaDataTransformer MetaDataTransformers}
 * are aware of the concrete meta-data implementation but they aren't aware of JSF components.
 * <p/>
 * The result of the transformation is used by
 * {@link be.rubus.web.jerry.component.ComponentInitializer ComponentInitializers}
 * to initialize JSF components based on the found meta-data.
 * <p/>
 * {@link be.rubus.web.jerry.metadata.CommonMetaDataKeys} provides the keys used by Jerry and Valerie.
 */
//*Transformer instead of *Converter to avoid naming confusion
public interface MetaDataTransformer {
    /**
     * Converts the information of a {@link MetaDataEntry} into an independent format.
     *
     * @param metaData The meta-data which should be converted.
     * @return Map with the converted information.
     */
    Map<String, Object> convertMetaData(MetaDataEntry metaData);
}
