package be.rubus.web.jerry.config;

import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.inject.Specializes;
import javax.faces.render.RenderKit;

/**
 *
 */
@Specializes
public class CustomJerryConfigurator extends JerryConfigurator {

    // This property will tell us if this custom configurator is called.
    public static RenderKit originalRenderKit;

    @Override
    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {
        originalRenderKit = renderKit;
        return super.getRenderKitWrapper(renderKit);
    }
}
