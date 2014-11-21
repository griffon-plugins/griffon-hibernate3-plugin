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

import griffon.plugins.hibernate3.Hibernate3Callback;
import griffon.plugins.hibernate3.Hibernate3Factory;
import griffon.plugins.hibernate3.Hibernate3Handler;
import griffon.plugins.hibernate3.Hibernate3Storage;
import griffon.plugins.hibernate3.exceptions.RuntimeHibernate3Exception;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultHibernate3Handler implements Hibernate3Handler {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultHibernate3Handler.class);
    private static final String ERROR_SESSION_FACTORY_NAME_BLANK = "Argument 'sessionFactoryName' must not be blank";
    private static final String ERROR_CALLBACK_NULL = "Argument 'callback' must not be null";

    private final Hibernate3Factory hibernate3Factory;
    private final Hibernate3Storage hibernate3Storage;

    @Inject
    public DefaultHibernate3Handler(@Nonnull Hibernate3Factory hibernate3Factory, @Nonnull Hibernate3Storage hibernate3Storage) {
        this.hibernate3Factory = requireNonNull(hibernate3Factory, "Argument 'hibernate3Factory' must not be null");
        this.hibernate3Storage = requireNonNull(hibernate3Storage, "Argument 'hibernate3Storage' must not be null");
    }

    @Nullable
    @Override
    public <R> R withHbm3Session(@Nonnull Hibernate3Callback<R> callback) throws RuntimeHibernate3Exception {
        return withHbm3Session(DefaultHibernate3Factory.KEY_DEFAULT, callback);
    }

    @Nullable
    @Override
    @SuppressWarnings("ThrowFromFinallyBlock")
    public <R> R withHbm3Session(@Nonnull String sessionFactoryName, @Nonnull Hibernate3Callback<R> callback) throws RuntimeHibernate3Exception {
        requireNonBlank(sessionFactoryName, ERROR_SESSION_FACTORY_NAME_BLANK);
        requireNonNull(callback, ERROR_CALLBACK_NULL);

        SessionFactory sf = getSessionFactory(sessionFactoryName);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing statements on session '{}'", sessionFactoryName);
        }
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            return callback.handle(sessionFactoryName, session);
        } catch (Exception e) {
            throw new RuntimeHibernate3Exception(sessionFactoryName, e);
        } finally {
            try {
                if (!session.getTransaction().wasRolledBack()) {
                    session.getTransaction().commit();
                }
                session.close();
            } catch (Exception e) {
                throw new RuntimeHibernate3Exception(sessionFactoryName, e);
            }
        }
    }

    @Override
    public void closeHbm3Session() {
        closeHbm3Session(DefaultHibernate3Factory.KEY_DEFAULT);
    }

    @Override
    public void closeHbm3Session(@Nonnull String sessionFactoryName) {
        requireNonBlank(sessionFactoryName, ERROR_SESSION_FACTORY_NAME_BLANK);
        SessionFactory hibernate3 = hibernate3Storage.get(sessionFactoryName);
        if (hibernate3 != null) {
            hibernate3Factory.destroy(sessionFactoryName, hibernate3);
            hibernate3Storage.remove(sessionFactoryName);
        }
    }

    @Nonnull
    private SessionFactory getSessionFactory(@Nonnull String sessionFactoryName) {
        SessionFactory sessionFactory = hibernate3Storage.get(sessionFactoryName);
        if (sessionFactory == null) {
            sessionFactory = hibernate3Factory.create(sessionFactoryName);
            hibernate3Storage.set(sessionFactoryName, sessionFactory);
        }
        return sessionFactory;
    }
}
