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
package org.codehaus.griffon.runtime.hibernate3;

import griffon.annotations.core.Nonnull;
import org.hibernate.Cache;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.TypeHelper;
import org.hibernate.engine.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class SessionFactoryDecorator implements SessionFactory {
    private final SessionFactory delegate;

    public SessionFactoryDecorator(@Nonnull SessionFactory delegate) {
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    @Nonnull
    protected SessionFactory getDelegate() {
        return delegate;
    }

    @Override
    public org.hibernate.classic.Session openSession() throws HibernateException {
        return delegate.openSession();
    }

    @Override
    public org.hibernate.classic.Session openSession(Interceptor interceptor) throws HibernateException {
        return delegate.openSession(interceptor);
    }

    @Override
    public org.hibernate.classic.Session openSession(Connection connection) {
        return delegate.openSession(connection);
    }

    @Override
    public org.hibernate.classic.Session openSession(Connection connection, Interceptor interceptor) {
        return delegate.openSession(connection, interceptor);
    }

    @Override
    public org.hibernate.classic.Session getCurrentSession() throws HibernateException {
        return delegate.getCurrentSession();
    }

    @Override
    public StatelessSession openStatelessSession() {
        return delegate.openStatelessSession();
    }

    @Override
    public StatelessSession openStatelessSession(Connection connection) {
        return delegate.openStatelessSession(connection);
    }

    @Override
    public ClassMetadata getClassMetadata(Class entityClass) {
        return delegate.getClassMetadata(entityClass);
    }

    @Override
    public ClassMetadata getClassMetadata(String entityName) {
        return delegate.getClassMetadata(entityName);
    }

    @Override
    public CollectionMetadata getCollectionMetadata(String roleName) {
        return delegate.getCollectionMetadata(roleName);
    }

    @Override
    public Map<String, ClassMetadata> getAllClassMetadata() {
        return delegate.getAllClassMetadata();
    }

    @Override
    public Map getAllCollectionMetadata() {
        return delegate.getAllCollectionMetadata();
    }

    @Override
    public Statistics getStatistics() {
        return delegate.getStatistics();
    }

    @Override
    public void close() throws HibernateException {
        delegate.close();
    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public void evict(Class persistentClass) throws HibernateException {
        delegate.evict(persistentClass);
    }

    @Override
    public void evict(Class persistentClass, Serializable id) throws HibernateException {
        delegate.evict(persistentClass, id);
    }

    @Override
    public void evictEntity(String entityName) throws HibernateException {
        delegate.evictEntity(entityName);
    }

    @Override
    public void evictEntity(String entityName, Serializable id) throws HibernateException {
        delegate.evictEntity(entityName, id);
    }

    @Override
    public void evictCollection(String roleName) throws HibernateException {
        delegate.evictCollection(roleName);
    }

    @Override
    public void evictCollection(String roleName, Serializable id) throws HibernateException {
        delegate.evictCollection(roleName, id);
    }

    @Override
    public void evictQueries(String cacheRegion) throws HibernateException {
        delegate.evictQueries(cacheRegion);
    }

    @Override
    public void evictQueries() throws HibernateException {
        delegate.evictQueries();
    }

    @Override
    public Set getDefinedFilterNames() {
        return delegate.getDefinedFilterNames();
    }

    @Override
    public FilterDefinition getFilterDefinition(String filterName) throws HibernateException {
        return delegate.getFilterDefinition(filterName);
    }

    @Override
    public boolean containsFetchProfileDefinition(String name) {
        return delegate.containsFetchProfileDefinition(name);
    }

    @Override
    public TypeHelper getTypeHelper() {
        return delegate.getTypeHelper();
    }

    @Override
    public Reference getReference() throws NamingException {
        return delegate.getReference();
    }
}
