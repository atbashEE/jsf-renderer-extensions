package be.rubus.web.jerry.renderkit;

import javax.enterprise.inject.Typed;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import javax.faces.render.Renderer;

/**
 *
 */
@Typed()
public class JerryRenderKit extends RenderKitWrapper {
    protected RenderKit wrapped;

    public JerryRenderKit(RenderKit wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public RenderKit getWrapped() {
        return wrapped;
    }

    public void addRenderer(String family, String rendererType, Renderer renderer) {
        if (renderer instanceof JerryRendererWrapper) {
            wrapped.addRenderer(family, rendererType, renderer);
        } else {
            wrapped.addRenderer(family, rendererType, createWrapper(renderer));
        }
    }

    public Renderer getRenderer(String family, String rendererType) {
        Renderer renderer = wrapped.getRenderer(family, rendererType);

        if (renderer != null) {
            return renderer instanceof JerryRendererWrapper ? renderer : createWrapper(renderer);
        }

        return null;
    }

    protected Renderer createWrapper(Renderer renderer) {
        return new JerryRendererWrapper(renderer);
    }

}
