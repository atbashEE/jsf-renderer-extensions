package be.rubus.web.jerry.storage;

import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.jerry.metadata.MetaDataHolder;
import be.rubus.web.jerry.metadata.MetaDataTransformer;
import be.rubus.web.jerry.provider.BeanProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;

/**
 *
 */
@ApplicationScoped
public class ComponentStorage {

    private List<MetaDataTransformer> transformers;

    private Map<StorageKey, MetaDataHolder> cache = new HashMap<>();
    private Set<StorageKey> entryPossible = new HashSet<>();
    private Map<ComponentKey, Map<String, Object>> componentInfo = new HashMap<>();

    @PostConstruct
    public void init() {
        transformers = BeanProvider.getContextualReferences(MetaDataTransformer.class, true, false);
    }

    public boolean containsEntry(String viewId, String clientId, Class<? extends MetaDataHolder> key) {
        return cache.containsKey(new StorageKey(viewId, clientId, key));
    }

    public void storeEntry(String viewId, String clientId, MetaDataHolder value) {
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
            if (!viewId.equals(that.viewId)) {
                return false;
            }

            return true;
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

            if (!key.equals(that.key)) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + key.hashCode();
            return result;
        }

    }
}
