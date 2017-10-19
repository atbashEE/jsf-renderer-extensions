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
 */
package be.atbash.ee.jsf.jerry.validation;

import be.atbash.ee.jsf.jerry.util.MavenDependencyUtil;
import be.atbash.ee.jsf.jerry.validation.custom.ZipCode;
import be.atbash.ee.jsf.jerry.view.CustomBean;
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
public class CustomTest {

    private static final String ARCHIVE_NAME = "customTest";
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
                .addClass(CustomBean.class)
                .addPackage(ZipCode.class.getPackage())
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("custom.xhtml", "custom.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testCustom() throws Exception {
        driver.get(new URL(contextPath, "custom.xhtml").toString());

        WebElement element = driver.findElement(By.id("test:zipCodeMaskLabel"));
        String text = element.getText();
        assertThat(text).isEqualTo("Zip code (mask)*");

        element = driver.findElement(By.id("test:zipCode"));
        assertThat(element.getAttribute("maxlength")).isEqualTo("4");

        element = driver.findElement(By.id("test:zipCodeMask"));

        element.sendKeys("azer");
        // Only numbers allowed in the mask
        assertThat(element.getAttribute("value")).isEqualTo("____");

        element.clear();
        element.sendKeys("12345");
        // Only 4 numbers allowed in the mask
        assertThat(element.getAttribute("value")).isEqualTo("1234");

    }

}
