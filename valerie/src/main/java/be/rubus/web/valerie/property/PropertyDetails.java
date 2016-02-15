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
 *
 */
package be.rubus.web.valerie.property;

/**
 */
public class PropertyDetails {
    //forms the id for cross-validation within complex components
    private String key;
    private Object baseObject;
    private String property;

    public PropertyDetails(String key, Object baseObject, String property) {
        this.key = key;
        this.baseObject = baseObject;
        this.property = property;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getBaseObject() {
        return baseObject;
    }

    public void setBaseObject(Object baseObject) {
        this.baseObject = baseObject;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PropertyDetails)) {
            return false;
        }

        PropertyDetails that = (PropertyDetails) o;

        if (baseObject != null ? !baseObject.equals(that.baseObject) : that.baseObject != null) {
            return false;
        }
        if (key != null ? !key.equals(that.key) : that.key != null) {
            return false;
        }
        return !(property != null ? !property.equals(that.property) : that.property != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (baseObject != null ? createNullAwareHashCode(baseObject) : 0);
        result = 31 * result + (property != null ? property.hashCode() : 0);
        return result;
    }

    private int createNullAwareHashCode(Object o) {
        try {
            return o.hashCode();
        } catch (NullPointerException e) {
            return 0;
        }
    }
}
