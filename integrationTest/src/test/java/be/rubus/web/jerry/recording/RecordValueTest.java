package be.rubus.web.jerry.recording;

import be.rubus.web.jerry.util.MavenDependencyUtil;
import be.rubus.web.jerry.view.DateRangeBean;
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
 * Valerie Test
 */
@RunWith(Arquillian.class)
public class RecordValueTest {
    private static final String ARCHIVE_NAME = "recordValueTest";
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
                .addClass(DateRangeBean.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("dateRange.xhtml", "dateRange.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testClassLevelValidation() throws Exception {
        driver.get(new URL(contextPath, "dateRange.xhtml").toString());

        WebElement field = driver.findElement(By.id("test:beginDate"));
        field.sendKeys("01/03/2015");

        field = driver.findElement(By.id("test:endDate"));
        field.sendKeys("01/01/2015");


        WebElement submitButton = driver.findElement(By.id("test:submit"));
        submitButton.click();

        Thread.sleep(1000);

        WebElement messages = driver.findElement(By.id("errors"));
        List<WebElement> errorElements = messages.findElements(By.tagName("li"));

        // FIXME The test fails and it seems that RecordingInfoPhaseListener doesn't kick in
        assertThat(errorElements).hasSize(1);

        assertThat(errorElements.get(0).getText()).contains("Validation Error: Value is required.");

    }

}
