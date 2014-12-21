package be.rubus.web.jerry.renderkit.model;

/**
 *
 */
public enum InterceptorCalls {
    BEFORE_ENCODE_BEGIN, AFTER_ENCODE_BEGIN,
    BEFORE_ENCODE_CHILDREN, AFTER_ENCODE_CHILDREN,
    BEFORE_ENCODE_END, AFTER_ENCODE_END,
    BEFORE_DECODE, AFTER_DECODE,
    BEFORE_CONVERTED_VALUE, AFTER_CONVERTED_VALUE
}
