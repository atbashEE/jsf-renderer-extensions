package be.rubus.web.valerie.provider;

import be.rubus.web.jerry.provider.BeanProvider;

import java.util.Date;

/**
 *
 */
public final class DateProducer {

    public static Date now() {
        Date result = tryFromProvider();
        if (result == null) {
            result = new Date();
        }
        return result;
    }

    private static Date tryFromProvider() {
        DateProvider provider = BeanProvider.getContextualReference(DateProvider.class, true);
        Date result = null;
        if (provider != null) {
            result = provider.now();
        }
        return result;
    }
}
