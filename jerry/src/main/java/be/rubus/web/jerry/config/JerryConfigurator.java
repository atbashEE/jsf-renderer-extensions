package be.rubus.web.jerry.config;

import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.render.RenderKit;

/**
 *
 */
@ApplicationScoped
public class JerryConfigurator {

    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {
        return new JerryRenderKit(renderKit);
    }

    public boolean useBeanInfo() {
        return true;
    }
}
