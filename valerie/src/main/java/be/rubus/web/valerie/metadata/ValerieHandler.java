package be.rubus.web.valerie.metadata;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

/**
 *
 */
public class ValerieHandler extends TagHandler {

    public ValerieHandler(TagConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        // TODO Valerie must be constant
        ValueExpression value = getAttribute("value").getValueExpression(ctx, Object.class);
        String attributeName = "Valerie" + getAttribute("for").getValue();
        parent.getAttributes().put(attributeName, value);
    }

}
