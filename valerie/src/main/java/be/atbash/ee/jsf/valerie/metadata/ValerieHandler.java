/*
 * Copyright 2014-2022 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jsf.valerie.metadata;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;
import java.io.IOException;

/**
 *
 */
public class ValerieHandler extends TagHandler {

    public static final String VALERIE = "Valerie";

    public ValerieHandler(TagConfig config) {
        super(config);
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        ValueExpression value = getAttribute("value").getValueExpression(ctx, Object.class);
        String attributeName = VALERIE + getAttribute("for").getValue();
        parent.getAttributes().put(attributeName, value);
    }

}
