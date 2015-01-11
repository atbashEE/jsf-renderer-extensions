package be.rubus.web.jerry.util;

import org.jboss.shrinkwrap.resolver.api.Resolvers;
import org.jboss.shrinkwrap.resolver.api.maven.ConfigurableMavenResolverSystem;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

import java.io.File;

/**
 *
 */
public final class MavenDependencyUtil {

    private static final String JERRY_CANONICAL_FORM = "be.rubus.web:jerry";
    private static final String ASSERTJ_CANONICAL_FORM = "org.assertj:assertj-core";

    private MavenDependencyUtil() {
    }

    public static File[] jerryFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(JERRY_CANONICAL_FORM).withoutTransitivity().asFile();

    }

    public static File[] assetJFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(ASSERTJ_CANONICAL_FORM).withoutTransitivity().asFile();

    }

    private static PomEquippedResolveStage getResolveStageFromPom() {
        return Resolvers.use(ConfigurableMavenResolverSystem.class).loadPomFromFile("pom.xml");
    }
}
