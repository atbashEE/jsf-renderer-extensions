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

/**
 * The {@link be.rubus.web.jerry.storage.ComponentStorage} is only aware of objects which are implementing this interface.
 * A plugin to Jerry can use any object structure as long as it follows the contracts of the MetaDataHolder.
 */
public interface MetaDataHolder {

    /**
     * Returns an immutable array which contains the
     * {@link be.rubus.web.jerry.metadata.MetaDataEntry}s which were created for the property.
     *
     * @return all {@link be.rubus.web.jerry.metadata.MetaDataEntry}s
     */
    MetaDataEntry[] getMetaDataEntries();

}
