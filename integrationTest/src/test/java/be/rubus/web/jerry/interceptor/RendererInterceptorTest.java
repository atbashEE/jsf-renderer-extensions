package be.rubus.web.jerry.interceptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * -Darquillian.jboss_home=/usr/local/opt/wildfly-as/libexec
 */
@RunWith(Arquillian.class)
public class RendererInterceptorTest {

    private static final String ARCHIVE_NAME = "rendererInterceptorTest";
    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @Deployment
    public static WebArchive deploy() {
        MavenStrategyStage jerryDependency = Resolvers.use(ConfigurableMavenResolverSystem.class).loadPomFromFile("pom.xml").resolve("be.rubus.web:jerry");

        return ShrinkWrap
                .create(WebArchive.class, ARCHIVE_NAME + ".war")
                .addAsLibraries(jerryDependency.withoutTransitivity().asFile())
                .addClass(MarkerRendererInterceptor.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("page.xhtml", "page.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @RunAsClient
    public void testInterceptor() throws Exception {
        driver.get(new URL(contextPath, "page.xhtml").toString());

        WebElement inputField = driver.findElement(By.id("test:valueInput"));
        String styleClass = inputField.getAttribute("class");
        assertThat(styleClass).isEqualTo("inputField");
    }
}
