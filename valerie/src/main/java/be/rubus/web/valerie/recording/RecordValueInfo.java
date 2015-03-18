package be.rubus.web.valerie.recording;

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
