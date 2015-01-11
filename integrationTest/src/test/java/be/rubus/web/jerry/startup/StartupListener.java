package be.rubus.web.jerry.startup;

import javax.enterprise.event.Observes;

/**
 *
 */
public class StartupListener {

    public static boolean startupReceived = false;

    public void onStartup(@Observes StartupEvent startupEvent) {
        startupReceived = true;
    }
}
