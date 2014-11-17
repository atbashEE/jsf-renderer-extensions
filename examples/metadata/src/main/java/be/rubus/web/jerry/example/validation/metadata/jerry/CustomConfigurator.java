package be.rubus.web.jerry.example.validation.metadata.jerry;

import be.rubus.web.jerry.config.JerryConfigurator;
import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.inject.Specializes;
import javax.faces.render.RenderKit;

/**
 *
 */
@Specializes
public class CustomConfigurator extends JerryConfigurator {
    @Override
    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {
        // We can here return our custom implementation"
        return super.getRenderKitWrapper(renderKit);
    }
}
