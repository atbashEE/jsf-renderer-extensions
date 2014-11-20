/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package be.rubus.web.valerie.property;


import be.rubus.web.jerry.metadata.MetaDataEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class DefaultPropertyInformation implements PropertyInformation {
    //protected final Logger logger = Logger.getLogger(getClass().getName());

    private Map<String, Object> informationMap = new HashMap<>();
    private List<MetaDataEntry> metaDataList = new ArrayList<>();

    public boolean containsInformation(String key) {
        return informationMap.containsKey(key);
    }

    public Object getInformation(String key) {
        return informationMap.get(key);
    }

    public <T> T getInformation(String key, Class<T> targetClass) {
        return (T) getInformation(key);
    }

    public void setInformation(String key, Object value) {
        //this.logger.finest("new information added key: " + key + " value: " + value);

        informationMap.put(key, value);
    }

    public MetaDataEntry[] getMetaDataEntries() {
        return metaDataList.toArray(new MetaDataEntry[metaDataList.size()]);
    }

    public void addMetaDataEntry(MetaDataEntry metaDataEntry) {
        metaDataEntry.setProperties(this.informationMap);
        this.metaDataList.add(metaDataEntry);
    }

    public void resetMetaDataEntries() {
        //this.logger.finest("resetting meta-data entries");

        this.metaDataList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultPropertyInformation)) {
            return false;
        }

        DefaultPropertyInformation that = (DefaultPropertyInformation) o;

        if (!informationMap.equals(that.informationMap)) {
            return false;
        }
        if (!metaDataList.equals(that.metaDataList)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = createNullAwareHashCode(informationMap);
        result = 31 * result + createNullAwareHashCode(metaDataList);
        return result;
    }

    private int createNullAwareHashCode(Object o) {
        return o == null ? 0 : o.hashCode();
    }
}
