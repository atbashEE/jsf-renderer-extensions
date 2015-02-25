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
import be.rubus.web.jerry.view.FutureBean;
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
 * Valerie test
 */
@RunWith(Arquillian.class)
public class FutureTest {

    private static final String ARCHIVE_NAME = "futureTest";
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
                .addClass(FutureBean.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("future_NoProvider.xhtml", "future_NoProvider.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testFuture() throws Exception {
        driver.get(new URL(contextPath, "future_NoProvider.xhtml").toString());

        WebElement date1 = driver.findElement(By.id("test:date1"));
        date1.sendKeys("23/02/2015");

        WebElement date2 = driver.findElement(By.id("test:date2"));
        date2.sendKeys("23/02/2015");

        WebElement btn = driver.findElement(By.id("test:submit"));
        btn.click();

        WebElement errors = driver.findElement(By.id("errors"));

        List<WebElement> errorMessages = errors.findElements(By.tagName("li"));
        assertThat(errorMessages).hasSize(2);

        // Page is reloaded so elements are detached

        date1 = driver.findElement(By.id("test:date1"));
        date2 = driver.findElement(By.id("test:date2"));

        assertThat(date1.getAttribute("class")).contains("ui-state-error");
        assertThat(date2.getAttribute("class")).contains("ui-state-error");
    }

}
