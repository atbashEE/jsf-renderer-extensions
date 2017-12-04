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
package be.atbash.ee.jsf.valerie.recording;

import javax.validation.ConstraintValidator;

/**
 *
 */
public class RecordValueInfo {

    private Class<?> targetClass;
    private Class<? extends ConstraintValidator> validator;
    private String classProperty;

    public RecordValueInfo(Class<?> targetClass, Class<? extends ConstraintValidator> validator) {
        this.targetClass = targetClass;
        this.validator = validator;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Class<? extends ConstraintValidator> getValidator() {
        return validator;
    }

    public void setClassProperty(String classProperty) {
        this.classProperty = classProperty;
    }

    public String getClassProperty() {
        return classProperty;
    }

    public Key getKey() {
        return new Key();
    }

    public class Key {

        public Class<?> getTargetClass() {
            return targetClass;
        }

        public Class<? extends ConstraintValidator> getValidator() {
            return validator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Key)) {
                return false;
            }

            Key key = (Key) o;

            if (!targetClass.equals(key.getTargetClass())) {
                return false;
            }
            if (!validator.equals(key.getValidator())) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = targetClass.hashCode();
            result = 31 * result + validator.hashCode();
            return result;
        }
    }
}
