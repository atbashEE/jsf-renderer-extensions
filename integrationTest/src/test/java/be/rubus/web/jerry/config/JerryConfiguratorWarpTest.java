package be.rubus.web.jerry.config;

import be.rubus.web.jerry.util.MavenDependencyUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.warp.Activity;
import org.jboss.arquillian.warp.Inspection;
import org.jboss.arquillian.warp.Warp;
import org.jboss.arquillian.warp.WarpTest;
import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.MavenStrategyStage;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@WarpTest
@RunAsClient
@RunWith(Arquillian.class)
public class JerryConfiguratorWarpTest {

    private static final String ARCHIVE_NAME = "jerryConfiguratorTest";

    @Drone
    private WebDriver driver;

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = true)
    public static WebArchive deploy() {
        return ShrinkWrap
                .create(WebArchive.class, ARCHIVE_NAME + ".war")
                .addAsLibraries(MavenDependencyUtil.jerryFiles())
                .addAsLibraries(MavenDependencyUtil.assetJFiles())
                .addClass(CustomJerryConfigurator.class)
                .addAsWebInfResource("default/WEB-INF/web.xml", "web.xml")
                .addAsWebResource("page.xhtml", "page.xhtml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testCustomConfiguration() throws Exception {


        Activity pageNavigation = new Activity() {
            @Override
            public void perform() {
                try {
                    driver.get(new URL(contextPath, "page.xhtml").toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        };
        InspectCustomJerryConfig inspectCustomJerryConfig = new InspectCustomJerryConfig();

        Warp.initiate(pageNavigation)
                .inspect(inspectCustomJerryConfig);


        //assertThat(inpectionResult.isConfigExecuted()).isTrue();
    }

    private static class InspectCustomJerryConfig extends Inspection {
        private static final long serialVersionUID = 1L;

        public InspectCustomJerryConfig() {
        }

        @AfterPhase(Phase.RENDER_RESPONSE)
        public void verifyRequest() {
            assertThat(CustomJerryConfigurator.originalRenderKit).isNotNull();
        }
    }
}