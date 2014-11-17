package be.rubus.web.jerry.renderkit;

import be.rubus.web.jerry.config.JerryConfigurator;
import be.rubus.web.jerry.provider.BeanProvider;

import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.Iterator;

/**
 *
 */
@Typed()
public class JerryRenderKitFactory extends RenderKitFactory {
    private RenderKitFactory wrapped;

    private JerryConfigurator configurator;

    public JerryRenderKitFactory(RenderKitFactory wrapped) {
        this.wrapped = wrapped;
        configurator = BeanProvider.getContextualReference(JerryConfigurator.class);
    }

    @Override
    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        wrapped.addRenderKit(renderKitId, renderKit);
    }

    @Override
    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        RenderKit renderKit = wrapped.getRenderKit(context, renderKitId);
        // TODO Is caching allowed here ? method is executed several times and thus each time a new wrapper is created.

        return configurator.getRenderKitWrapper(renderKit);
    }

    @Override
    public Iterator<String> getRenderKitIds() {
        return wrapped.getRenderKitIds();
    }
}
