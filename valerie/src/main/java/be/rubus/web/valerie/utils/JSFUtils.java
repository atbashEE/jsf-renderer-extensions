package be.rubus.web.valerie.utils;

import be.rubus.web.valerie.el.ValueBindingExpression;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.Externalizable;

/**
 *
 */
public final class JSFUtils {

    private JSFUtils() {
    }


    public static boolean isELTermWellFormed(Object o) {
        if (o instanceof ValueBinding || o instanceof Externalizable) {
            return false;
        }

        String s = o.toString();
        return ((s.contains("#") || s.contains("$")) && s.contains("{") && s.contains("}"));
    }

    public static Object getBindingOfComponent(UIComponent uiComponent, String name) {
        return uiComponent.getValueExpression(name);
    }

    public static Class getTypeOfExpression(FacesContext facesContext, ValueBindingExpression valueBindingExpression) {
        //due to a restriction with the ri
        Object bean = getValueOfExpression(facesContext, valueBindingExpression);
        return (bean != null) ? ProxyUtils.getUnproxiedClass(bean.getClass()) : null;
    }

    public static Object getValueOfExpression(FacesContext facesContext,
                                              ValueBindingExpression valueBindingExpression) {
        return (valueBindingExpression != null) ? facesContext.getApplication().evaluateExpressionGet(
                facesContext, valueBindingExpression.getExpressionString(), Object.class) : null;
    }
}
