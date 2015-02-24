/*
 * Copyright 2014-2015 Rudy De Busscher
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
import be.rubus.web.jerry.view.CombinedBean;
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
public class CombinedTest {

    private static final String ARCHIVE_NAME = "maxSizeTest";
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
                .addClass(CombinedBean.class)
                .addClass(CombinedValidation.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("combined.xhtml", "combined.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testCombined() throws Exception {
        driver.get(new URL(contextPath, "combined.xhtml").toString());

        WebElement element = driver.findElement(By.id("test:value"));
        assertThat(element.getAttribute("maxlength")).isNull();

        element = driver.findElement(By.id("test:valueLabel"));
        String text = element.getText();
        assertThat(text).isEqualTo("value");

        element = driver.findElement(By.id("test:required"));
        assertThat(element.getAttribute("maxlength")).isNull();

        element = driver.findElement(By.id("test:requiredLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("required*");

        element = driver.findElement(By.id("test:maxLength"));
        assertThat(element.getAttribute("maxlength")).isEqualTo("14");

        element = driver.findElement(By.id("test:maxLengthLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("max length");

        element = driver.findElement(By.id("test:combined"));
        assertThat(element.getAttribute("maxlength")).isEqualTo("14");

        element = driver.findElement(By.id("test:combinedLabel"));
        text = element.getText();
        assertThat(text).isEqualTo("combined*");


    }

}
