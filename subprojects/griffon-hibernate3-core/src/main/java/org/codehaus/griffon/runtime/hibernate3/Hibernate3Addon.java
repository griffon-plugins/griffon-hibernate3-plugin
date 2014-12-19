/*
 * Copyright 2014 the original author or authors.
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
package org.codehaus.griffon.runtime.hibernate3;

import griffon.core.GriffonApplication;
import griffon.inject.DependsOn;
import griffon.plugins.hibernate3.Hibernate3Callback;
import griffon.plugins.hibernate3.Hibernate3Factory;
import griffon.plugins.hibernate3.Hibernate3Handler;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.hibernate.Session;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

import static griffon.util.ConfigUtils.getConfigValueAsBoolean;

/**
 * @author Andres Almiray
 * @since 1.1.0
 */
@DependsOn("datasource")
@Named("hibernate3")
public class Hibernate3Addon extends AbstractGriffonAddon {
    @Inject
    private Hibernate3Handler hibernate3Handler;

    @Inject
    private Hibernate3Factory hibernate3Factory;

    public void onBootstrapEnd(@Nonnull GriffonApplication application) {
        for (String sessionFactoryName : hibernate3Factory.getSessionFactoryNames()) {
            Map<String, Object> config = hibernate3Factory.getConfigurationFor(sessionFactoryName);
            if (getConfigValueAsBoolean(config, "connect_on_startup", false)) {
                hibernate3Handler.withHbm3Session(sessionFactoryName, new Hibernate3Callback<Void>() {
                    @Override
                    public Void handle(@Nonnull String sessionFactoryName, @Nonnull Session session) {
                        return null;
                    }
                });
            }
        }
    }

    public void onShutdownStart(@Nonnull GriffonApplication application) {
        for (String sessionFactoryName : hibernate3Factory.getSessionFactoryNames()) {
            hibernate3Handler.closeHbm3Session(sessionFactoryName);
        }
    }
}
