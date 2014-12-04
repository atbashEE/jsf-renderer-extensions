package be.rubus.web.jerry.startup;

import be.rubus.web.jerry.provider.BeanManagerProvider;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

/**
 *
 */
public class SystemStartup implements SystemEventListener {
    @Override
    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        BeanManagerProvider.getInstance().getBeanManager().fireEvent(new StartupEvent());
    }

    @Override
    public boolean isListenerForSource(Object o) {
        return true;
    }
}
