package be.rubus.web.jerry.config;

import be.rubus.web.jerry.config.logging.ConfigEntry;
import be.rubus.web.jerry.config.logging.ModuleConfig;
import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.render.RenderKit;

/**
 *
 */
@ApplicationScoped
public class JerryConfigurator implements ModuleConfig {


    @ConfigEntry(classResult = JerryRenderKit.class)
    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {

        return new JerryRenderKit(renderKit);
    }

    @ConfigEntry
    public boolean useBeanInfo() {
        return true;
    }
}
