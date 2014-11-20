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
