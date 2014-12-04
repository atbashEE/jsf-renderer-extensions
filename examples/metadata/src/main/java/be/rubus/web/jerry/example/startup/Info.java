package be.rubus.web.jerry.example.startup;

import be.rubus.web.jerry.startup.StartupEvent;

import javax.enterprise.event.Observes;

/**
 *
 */
public class Info {

    public void starting(@Observes StartupEvent event) {
        System.out.println("Application is started");
    }
}
