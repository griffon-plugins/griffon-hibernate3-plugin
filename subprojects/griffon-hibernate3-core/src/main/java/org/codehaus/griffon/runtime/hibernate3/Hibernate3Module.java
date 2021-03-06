/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2021 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.hibernate3;

import griffon.annotations.inject.DependsOn;
import griffon.core.Configuration;
import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import griffon.plugins.hibernate3.Hibernate3Factory;
import griffon.plugins.hibernate3.Hibernate3Handler;
import griffon.plugins.hibernate3.Hibernate3Storage;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.codehaus.griffon.runtime.util.ResourceBundleProvider;
import org.kordamp.jipsy.annotations.ServiceProviderFor;

import javax.inject.Named;
import java.util.ResourceBundle;

import static griffon.util.AnnotationUtils.named;

/**
 * @author Andres Almiray
 */
@DependsOn("datasource")
@Named("hibernate3")
@ServiceProviderFor(Module.class)
public class Hibernate3Module extends AbstractModule {
    @Override
    protected void doConfigure() {
        // tag::bindings[]
        bind(ResourceBundle.class)
            .withClassifier(named("hibernate3"))
            .toProvider(new ResourceBundleProvider("Hibernate3"))
            .asSingleton();

        bind(Configuration.class)
            .withClassifier(named("hibernate3"))
            .to(DefaultHibernate3Configuration.class)
            .asSingleton();

        bind(Hibernate3Storage.class)
            .to(DefaultHibernate3Storage.class)
            .asSingleton();

        bind(Hibernate3Factory.class)
            .to(DefaultHibernate3Factory.class)
            .asSingleton();

        bind(Hibernate3Handler.class)
            .to(DefaultHibernate3Handler.class)
            .asSingleton();

        bind(GriffonAddon.class)
            .to(Hibernate3Addon.class)
            .asSingleton();
        // end::bindings[]
    }
}
