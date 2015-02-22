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
    private static final String VALERIE_CANONICAL_FORM = "be.rubus.web:valerie";
    private static final String VALERIE_PRIMEFACES_CANONICAL_FORM = "be.rubus.web.valerie:primefaces";
    private static final String ASSERTJ_CANONICAL_FORM = "org.assertj:assertj-core";
    private static final String PRIMEFACES_CANONICAL_FORM = "org.primefaces:primefaces";

    private MavenDependencyUtil() {
    }

    public static File[] jerryFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(JERRY_CANONICAL_FORM).withoutTransitivity().asFile();

    }

    public static File[] valerieFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(VALERIE_CANONICAL_FORM).withoutTransitivity().asFile();

    }

    public static File[] valeriePrimeFacesFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(VALERIE_PRIMEFACES_CANONICAL_FORM).withoutTransitivity().asFile();
    }

    public static File[] assertJFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(ASSERTJ_CANONICAL_FORM).withoutTransitivity().asFile();
    }

    public static File[] primeFacesFiles() {
        PomEquippedResolveStage pomEquippedResolveStage = getResolveStageFromPom();
        return pomEquippedResolveStage.resolve(PRIMEFACES_CANONICAL_FORM).withoutTransitivity().asFile();

    }

    private static PomEquippedResolveStage getResolveStageFromPom() {
        return Resolvers.use(ConfigurableMavenResolverSystem.class).loadPomFromFile("pom.xml");
    }
}
