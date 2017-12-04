/*
 * Copyright 2014-2016 Rudy De Busscher
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

import be.atbash.ee.jsf.jerry.metadata.MetaDataEntry;
import be.atbash.ee.jsf.jerry.metadata.MetaDataHolder;
import be.atbash.ee.jsf.jerry.metadata.MetaDataTransformer;
import be.atbash.ee.jsf.jerry.util.cdi.BeanManagerFake;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentStorageTest {

    private static final String VIEW_ID = "viewId";
    private static final String CLIENT_ID = "clientId";

    private static final String OTHER_VIEW_ID = "otherViewId";
    private static final String OTHER_CLIENT_ID = "otherClientId";

    private static final String ENTRY_KEY = "entryKey";
    private static final String ENTRY_TRANS_KEY = "entryTransKey";
    private static final String ENTRY_VALUE = "entryValue";
    private static final String OTHER_ENTRY_KEY = "otherEntryKey";
    private static final String OTHER_ENTRY_VALUE = "otherEntryValue";

    private BeanManagerFake beanManagerFake;

    private ComponentStorage componentStorage = new ComponentStorage();

    @Test
    public void testContainsEntry() {
        // So that enhancers get initialized with empty list
        initializeEmptyBeanManager();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_VALUE);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        assertThat(componentStorage.containsEntry(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    private void initializeEmptyBeanManager() {
        beanManagerFake = new BeanManagerFake();
        beanManagerFake.endRegistration();
        componentStorage.init();
    }

    @Test
    public void testContainsEntry_NotSet() {

        assertThat(componentStorage.containsEntry(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testContainsEntry_OtherViewId() {
        // So that enhancers get initialized with empty list
        initializeEmptyBeanManager();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_VALUE);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        assertThat(componentStorage.containsEntry(OTHER_VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testContainsEntry_OtherClientId() {
        // So that enhancers get initialized with empty list
        initializeEmptyBeanManager();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_VALUE);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        assertThat(componentStorage.containsEntry(VIEW_ID, OTHER_CLIENT_ID, TestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testContainsEntry_OtherViewAndClientId() {
        // So that enhancers get initialized with empty list
        initializeEmptyBeanManager();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_VALUE);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        assertThat(componentStorage.containsEntry(OTHER_VIEW_ID, OTHER_CLIENT_ID, TestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testContainsEntry_OtherMetaData() {
        // So that enhancers get initialized with empty list
        initializeEmptyBeanManager();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_VALUE);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        assertThat(componentStorage.containsEntry(VIEW_ID, CLIENT_ID, OtherTestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testIsEntryPossibleFor() {

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    @Test
    public void testIsEntryPossibleFor_NotPossibleSet() {
        componentStorage.setNotAvailable(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class);

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isFalse();
    }

    @Test
    public void testIsEntryPossibleFor_NotPossibleSetOtherMetaData() {
        componentStorage.setNotAvailable(VIEW_ID, CLIENT_ID, OtherTestMetaDataHolder.class);

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    @Test
    public void testIsEntryPossibleFor_NotPossibleSetOtherView() {
        componentStorage.setNotAvailable(OTHER_VIEW_ID, CLIENT_ID, TestMetaDataHolder.class);

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    @Test
    public void testIsEntryPossibleFor_NotPossibleSetOtherClient() {
        componentStorage.setNotAvailable(VIEW_ID, OTHER_CLIENT_ID, TestMetaDataHolder.class);

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    @Test
    public void testIsEntryPossibleFor_NotPossibleSetOtherViewAndClient() {
        componentStorage.setNotAvailable(OTHER_VIEW_ID, OTHER_CLIENT_ID, TestMetaDataHolder.class);

        assertThat(componentStorage.isEntryPossibleFor(VIEW_ID, CLIENT_ID, TestMetaDataHolder.class)).isTrue();
    }

    @Test
    public void testGetComponentInfo() {

        beanManagerFake = new BeanManagerFake();
        beanManagerFake.registerBean(new DummyMetaDataTransformer(), MetaDataTransformer.class);
        beanManagerFake.endRegistration();

        componentStorage.init();

        MetaDataHolder value1 = new TestMetaDataHolder(OTHER_ENTRY_KEY, OTHER_ENTRY_KEY);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value1);

        // This will override previous one, so we test for this one.
        MetaDataHolder value2 = new TestMetaDataHolder(ENTRY_KEY, ENTRY_KEY);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value2);

        Map<String, Object> componentInfo = componentStorage.getComponentInfo(VIEW_ID, CLIENT_ID);
        assertThat(componentInfo).isNotNull();
        assertThat(componentInfo.entrySet()).isNotEmpty();

        assertThat(componentInfo.keySet()).containsExactly(ENTRY_TRANS_KEY);
    }

    @Test
    public void testGetComponentInfo_isCached() {

        beanManagerFake = new BeanManagerFake();
        DummyMetaDataTransformer transformer = new DummyMetaDataTransformer();
        beanManagerFake.registerBean(transformer, MetaDataTransformer.class);
        beanManagerFake.endRegistration();

        componentStorage.init();

        MetaDataHolder value = new TestMetaDataHolder(ENTRY_KEY, ENTRY_KEY);
        componentStorage.storeEntry(VIEW_ID, CLIENT_ID, value);

        Map<String, Object> componentInfo = componentStorage.getComponentInfo(VIEW_ID, CLIENT_ID);
        assertThat(componentInfo).isNotNull();
        assertThat(componentInfo.entrySet()).isNotEmpty();

        componentInfo = componentStorage.getComponentInfo(VIEW_ID, CLIENT_ID);
        assertThat(componentInfo).isNotNull();
        assertThat(componentInfo.entrySet()).isNotEmpty();
        assertThat(transformer.getExecuted()).isEqualTo(1);

    }

    @Test
    public void testGetComponentInfo_NoEntryStored() {
        Map<String, Object> componentInfo = componentStorage.getComponentInfo(VIEW_ID, CLIENT_ID);
        assertThat(componentInfo).isNotNull();
        assertThat(componentInfo.entrySet()).isEmpty();
    }

    private static class TestMetaDataHolder implements MetaDataHolder {

        private String entryKey;
        private String entryValue;

        private TestMetaDataHolder(String entryKey, String entryValue) {
            this.entryKey = entryKey;
            this.entryValue = entryValue;
        }

        // Jerry doesn't contain any of these, mainly designed for Valerie.
        @Override
        public MetaDataEntry[] getMetaDataEntries() {
            MetaDataEntry testEntry = new MetaDataEntry();
            testEntry.setKey(entryKey);
            testEntry.setValue(entryValue);
            return new MetaDataEntry[]{testEntry};
        }
    }

    private static class OtherTestMetaDataHolder implements MetaDataHolder {
        // Jerry doesn't contain any of these, mainly designed for Valerie.
        @Override
        public MetaDataEntry[] getMetaDataEntries() {
            return new MetaDataEntry[0];
        }
    }

    private static class DummyMetaDataTransformer implements MetaDataTransformer {

        private int executed;

        @Override
        public Map<String, Object> convertMetaData(MetaDataEntry metaData) {
            executed++;
            Map<String, Object> result = new HashMap<>();
            if (metaData != null) {
                if (ENTRY_KEY.equals(metaData.getKey())) {
                    result.put(ENTRY_TRANS_KEY, metaData.getValue());
                }
            }
            return result;
        }

        public int getExecuted() {
            return executed;
        }
    }
}