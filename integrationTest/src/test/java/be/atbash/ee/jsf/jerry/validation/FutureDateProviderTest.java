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
import be.atbash.ee.jsf.jerry.view.DateProviderBean;
import be.atbash.ee.jsf.jerry.view.FutureBean;
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
public class FutureDateProviderTest {

    private static final String ARCHIVE_NAME = "futureDateProviderTest";
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
                .addClass(DateProviderBean.class)
                .addClass(FutureBean.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("future.xhtml", "future.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testFuture() throws Exception {
        driver.get(new URL(contextPath, "future.xhtml").toString());

        //By default, the date provider is using system date, so failures
        WebElement date1 = driver.findElement(By.id("test:date1"));
        date1.sendKeys("23/02/2015");

        WebElement date2 = driver.findElement(By.id("test:date2"));
        date2.sendKeys("23/02/2015");

        assertThat(date1.getAttribute("class")).doesNotContain("ui-state-error");
        assertThat(date2.getAttribute("class")).doesNotContain("ui-state-error");


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

        // Set date for DateProvider
        WebElement fixedNow = driver.findElement(By.id("date:fixedNow"));
        fixedNow.clear();
        fixedNow.sendKeys("20/02/2015");

        WebElement dateBtn = driver.findElement(By.id("date:setDate"));
        dateBtn.click();

        date1 = driver.findElement(By.id("test:date1"));
        date1.sendKeys("23/02/2015");

        date2 = driver.findElement(By.id("test:date2"));
        date2.sendKeys("23/02/2015");

        btn = driver.findElement(By.id("test:submit"));
        btn.click();

        errors = driver.findElement(By.id("errors"));

        errorMessages = errors.findElements(By.tagName("li"));
        assertThat(errorMessages).hasSize(1);  // Only 1 error now

        // Page is reloaded so elements are detached

        date1 = driver.findElement(By.id("test:date1"));
        date2 = driver.findElement(By.id("test:date2"));

        assertThat(date1.getAttribute("class")).contains("ui-state-error");
        assertThat(date2.getAttribute("class")).doesNotContain("ui-state-error");  // The one withValFuture

    }

}
