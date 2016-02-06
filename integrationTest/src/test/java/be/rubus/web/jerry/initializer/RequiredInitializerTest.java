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
package be.rubus.web.jerry.initializer;

import be.rubus.web.jerry.util.MavenDependencyUtil;
import be.rubus.web.jerry.view.ValerieBean;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  Valerie Test
 */
@RunWith(Arquillian.class)
public class RequiredInitializerTest {

    private static final String ARCHIVE_NAME = "requiredInitializerTest";
    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;


    @Deployment
    public static WebArchive deploy() {


        return ShrinkWrap
                .create(WebArchive.class, ARCHIVE_NAME + ".war")
                .addAsLibraries(MavenDependencyUtil.valerieFiles())
                .addAsLibraries(MavenDependencyUtil.jerryFiles())
                .addClass(ValerieBean.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("validations.xhtml", "validations.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testRequired() throws Exception {
        driver.get(new URL(contextPath, "validations.xhtml").toString());

        WebElement submitButton = driver.findElement(By.id("test:submit"));
        submitButton.click();

        Thread.sleep(1000);

        WebElement messages = driver.findElement(By.id("messages"));
        List<WebElement> errorElements = messages.findElements(By.tagName("li"));
        assertThat(errorElements).hasSize(1);

        assertThat(errorElements.get(0).getText()).contains("Validation Error: Value is required.");

    }

}
