/*
 * Copyright 2014-2016 Rudy De Busscher
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
 *
 */
package be.rubus.web.jerry.validation;

import be.rubus.web.jerry.util.MavenDependencyUtil;
import be.rubus.web.jerry.view.RequiredBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Valerie test
 */
@RunWith(Arquillian.class)
public class RequiredTest {

    private static final String ARCHIVE_NAME = "requiredTest";
    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;


    @Deployment
    public static WebArchive deploy() {

        return ShrinkWrap
                .create(WebArchive.class, ARCHIVE_NAME + ".war")
                        // We don't do transitive dependencies, so manually adding them
                .addAsLibraries(MavenDependencyUtil.valerieFiles())
                .addAsLibraries(MavenDependencyUtil.valeriePrimeFacesFiles())
                .addAsLibraries(MavenDependencyUtil.jerryFiles())
                .addAsLibraries(MavenDependencyUtil.primeFacesFiles())
                .addClass(RequiredBean.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("required.xhtml", "required.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testRequired() throws Exception {
        driver.get(new URL(contextPath, "required.xhtml").toString());

        WebElement element = driver.findElement(By.id("test:notRequiredLabel"));
        String text = element.getText();
        assertThat(text).isEqualTo("notRequired");

        element = driver.findElement(By.id("test:requiredLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("required*");

        element = driver.findElement(By.id("test:sizeLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("size");

        element = driver.findElement(By.id("test:specialLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("special*");

    }

}
