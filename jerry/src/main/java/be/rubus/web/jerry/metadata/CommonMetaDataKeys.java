package be.rubus.web.jerry.metadata;

/**
 *
 */
public enum CommonMetaDataKeys {
   REQUIRED("required");

    private String key;

    CommonMetaDataKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
