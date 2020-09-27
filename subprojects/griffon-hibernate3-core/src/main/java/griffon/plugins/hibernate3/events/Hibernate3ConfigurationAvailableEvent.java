/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2014-2020 The author and/or original authors.
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
package griffon.plugins.hibernate3.events;

import griffon.annotations.core.Nonnull;
import griffon.core.event.Event;

import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 * @since 3.0.0
 */
public class Hibernate3ConfigurationAvailableEvent extends Event {
    private final Map<String, Object> config;

    public Hibernate3ConfigurationAvailableEvent(@Nonnull Map<String, Object> config) {
        this.config = requireNonNull(config, "Argument 'config' must not be null");
    }

    @Nonnull
    public Map<String, Object> getConfig() {
        return config;
    }

    @Nonnull
    public static Hibernate3ConfigurationAvailableEvent of(@Nonnull Map<String, Object> config) {
        return new Hibernate3ConfigurationAvailableEvent(config);
    }
}