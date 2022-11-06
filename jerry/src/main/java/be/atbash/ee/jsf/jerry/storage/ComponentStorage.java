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
package be.atbash.ee.jsf.jerry.storage;

import be.atbash.ee.jsf.jerry.metadata.*;
import be.atbash.util.CDIUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;

/**
 *
 */
@ApplicationScoped
public class ComponentStorage {

    private List<MetaDataEnhancer> enhancers;
    private List<MetaDataTransformer> transformers;

    private Map<StorageKey, MetaDataHolder> cache = new HashMap<>();
    private Set<StorageKey> entryPossible = new HashSet<>();
    private Map<ComponentKey, Map<String, Object>> componentInfo = new HashMap<>();

    @PostConstruct
    public void init() {
        transformers = CDIUtils.retrieveInstances(MetaDataTransformer.class);
        enhancers = CDIUtils.retrieveInstances(MetaDataEnhancer.class);
    }

    public boolean containsEntry(String viewId, String clientId, Class<? extends MetaDataHolder> key) {
        return cache.containsKey(new StorageKey(viewId, clientId, key));
    }

    public void storeEntry(String viewId, String clientId, MetaDataHolder value) {
        for (MetaDataEnhancer enhancer : enhancers) {
            enhancer.enhanceData(value);
        }
        cache.put(new StorageKey(viewId, clientId, value.getClass()), value);
    }

    public <T extends MetaDataHolder> T getEntry(String viewId, String clientId, Class<T> key) {
        return (T) cache.get(new StorageKey(viewId, clientId, key));
    }

    public boolean isEntryPossibleFor(String viewId, String clientId, Class<? extends MetaDataHolder> key) {
        return !entryPossible.contains(new StorageKey(viewId, clientId, key));
    }

    public void setNotAvailable(String viewId, String clientId, Class<? extends MetaDataHolder> key) {
        entryPossible.add(new StorageKey(viewId, clientId, key));
    }

    public Map<String, Object> getComponentInfo(String viewId, String clientId) {
        ComponentKey key = new ComponentKey(viewId, clientId);
        Map<String, Object> result = componentInfo.get(key);
        if (result == null) {

            result = transformMetaData(getAllMetaDataHolders(key));
            componentInfo.put(key, result);
        }
        return result;
    }

    private Map<String, Object> transformMetaData(List<MetaDataHolder> allMetaDataHolders) {
        Map<String, Object> result = new HashMap<>();
        for (MetaDataHolder holder : allMetaDataHolders) {
            for (MetaDataEntry entry : holder.getMetaDataEntries()) {
                for (MetaDataTransformer transformer : transformers) {

                    result.putAll(transformer.convertMetaData(entry));
                }
            }
        }
        return result;
    }

    private List<MetaDataHolder> getAllMetaDataHolders(ComponentKey key) {
        List<MetaDataHolder> result = new ArrayList<>();
        for (Map.Entry<StorageKey, MetaDataHolder> entry : cache.entrySet()) {
            if (entry.getKey().isMatchingComponentKey(key)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    public List<?> getRecordingInformation(String viewId, String clientId) {
        List<?> result = new ArrayList<>();
        ComponentKey key = new ComponentKey(viewId, clientId);
        List<MetaDataHolder> allMetaDataHolders = getAllMetaDataHolders(key);
        for (MetaDataHolder holder : allMetaDataHolders) {
            for (MetaDataEntry entry : holder.getMetaDataEntries()) {
                List property = entry.getProperty(PropertyInformationKeys.RECORDING_INFORMATION, List.class);
                if (property != null) {
                    result.addAll(property);
                }
            }
        }
        return result;
    }

    private static class ComponentKey {
        private String viewId;
        private String clientId;

        public ComponentKey(String viewId, String clientId) {
            this.viewId = viewId;
            this.clientId = clientId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ComponentKey)) {
                return false;
            }

            ComponentKey that = (ComponentKey) o;

            if (!clientId.equals(that.clientId)) {
                return false;
            }
            return viewId.equals(that.viewId);

        }

        @Override
        public int hashCode() {
            int result = viewId.hashCode();
            result = 31 * result + clientId.hashCode();
            return result;
        }
    }

    private static class StorageKey extends ComponentKey {

        private Class<? extends MetaDataHolder> key;

        public StorageKey(String viewId, String clientId, Class<? extends MetaDataHolder> key) {
            super(viewId, clientId);
            this.key = key;
        }

        public boolean isMatchingComponentKey(ComponentKey componentKey) {
            return super.equals(componentKey);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof StorageKey)) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }

            StorageKey that = (StorageKey) o;

            return key.equals(that.key);

        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + key.hashCode();
            return result;
        }

    }
}
