/*
 * Copyright 2014-2015 the original author or authors.
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

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class SessionDecorator implements Session {
    private final Session delegate;

    public SessionDecorator(@Nonnull Session delegate) {
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    @Nonnull
    protected Session getDelegate() {
        return delegate;
    }

    @Override
    public Object saveOrUpdateCopy(Object object) throws HibernateException {
        return delegate.saveOrUpdateCopy(object);
    }

    @Override
    public Object saveOrUpdateCopy(Object object, Serializable id) throws HibernateException {
        return delegate.saveOrUpdateCopy(object, id);
    }

    @Override
    public Object saveOrUpdateCopy(String entityName, Object object) throws HibernateException {
        return delegate.saveOrUpdateCopy(entityName, object);
    }

    @Override
    public Object saveOrUpdateCopy(String entityName, Object object, Serializable id) throws HibernateException {
        return delegate.saveOrUpdateCopy(entityName, object, id);
    }

    @Override
    public List find(String query) throws HibernateException {
        return delegate.find(query);
    }

    @Override
    public List find(String query, Object value, Type type) throws HibernateException {
        return delegate.find(query, value, type);
    }

    @Override
    public List find(String query, Object[] values, Type[] types) throws HibernateException {
        return delegate.find(query, values, types);
    }

    @Override
    public Iterator iterate(String query) throws HibernateException {
        return delegate.iterate(query);
    }

    @Override
    public Iterator iterate(String query, Object value, Type type) throws HibernateException {
        return delegate.iterate(query, value, type);
    }

    @Override
    public Iterator iterate(String query, Object[] values, Type[] types) throws HibernateException {
        return delegate.iterate(query, values, types);
    }

    @Override
    public Collection filter(Object collection, String filter) throws HibernateException {
        return delegate.filter(collection, filter);
    }

    @Override
    public Collection filter(Object collection, String filter, Object value, Type type) throws HibernateException {
        return delegate.filter(collection, filter, value, type);
    }

    @Override
    public Collection filter(Object collection, String filter, Object[] values, Type[] types) throws HibernateException {
        return delegate.filter(collection, filter, values, types);
    }

    @Override
    public int delete(String query) throws HibernateException {
        return delegate.delete(query);
    }

    @Override
    public int delete(String query, Object value, Type type) throws HibernateException {
        return delegate.delete(query, value, type);
    }

    @Override
    public int delete(String query, Object[] values, Type[] types) throws HibernateException {
        return delegate.delete(query, values, types);
    }

    @Override
    @Deprecated
    public Query createSQLQuery(String sql, String returnAlias, Class returnClass) {
        return delegate.createSQLQuery(sql, returnAlias, returnClass);
    }

    @Override
    @Deprecated
    public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses) {
        return delegate.createSQLQuery(sql, returnAliases, returnClasses);
    }

    @Override
    public void save(Object object, Serializable id) throws HibernateException {
        delegate.save(object, id);
    }

    @Override
    public void save(String entityName, Object object, Serializable id) throws HibernateException {
        delegate.save(entityName, object, id);
    }

    @Override
    public void update(Object object, Serializable id) throws HibernateException {
        delegate.update(object, id);
    }

    @Override
    public void update(String entityName, Object object, Serializable id) throws HibernateException {
        delegate.update(entityName, object, id);
    }

    @Override
    public EntityMode getEntityMode() {
        return delegate.getEntityMode();
    }

    @Override
    public org.hibernate.Session getSession(EntityMode entityMode) {
        return delegate.getSession(entityMode);
    }

    @Override
    public void flush() throws HibernateException {
        delegate.flush();
    }

    @Override
    public void setFlushMode(FlushMode flushMode) {
        delegate.setFlushMode(flushMode);
    }

    @Override
    public FlushMode getFlushMode() {
        return delegate.getFlushMode();
    }

    @Override
    public void setCacheMode(CacheMode cacheMode) {
        delegate.setCacheMode(cacheMode);
    }

    @Override
    public CacheMode getCacheMode() {
        return delegate.getCacheMode();
    }

    @Override
    public SessionFactory getSessionFactory() {
        return delegate.getSessionFactory();
    }

    @Override
    public Connection connection() throws HibernateException {
        return delegate.connection();
    }

    @Override
    public Connection close() throws HibernateException {
        return delegate.close();
    }

    @Override
    public void cancelQuery() throws HibernateException {
        delegate.cancelQuery();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public boolean isConnected() {
        return delegate.isConnected();
    }

    @Override
    public boolean isDirty() throws HibernateException {
        return delegate.isDirty();
    }

    @Override
    public boolean isDefaultReadOnly() {
        return delegate.isDefaultReadOnly();
    }

    @Override
    public void setDefaultReadOnly(boolean readOnly) {
        delegate.setDefaultReadOnly(readOnly);
    }

    @Override
    public Serializable getIdentifier(Object object) throws HibernateException {
        return delegate.getIdentifier(object);
    }

    @Override
    public boolean contains(Object object) {
        return delegate.contains(object);
    }

    @Override
    public void evict(Object object) throws HibernateException {
        delegate.evict(object);
    }

    @Override
    public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
        return delegate.load(theClass, id, lockMode);
    }

    @Override
    public Object load(Class theClass, Serializable id, LockOptions lockOptions) throws HibernateException {
        return delegate.load(theClass, id, lockOptions);
    }

    @Override
    public Object load(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return delegate.load(entityName, id, lockMode);
    }

    @Override
    public Object load(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
        return delegate.load(entityName, id, lockOptions);
    }

    @Override
    public Object load(Class theClass, Serializable id) throws HibernateException {
        return delegate.load(theClass, id);
    }

    @Override
    public Object load(String entityName, Serializable id) throws HibernateException {
        return delegate.load(entityName, id);
    }

    @Override
    public void load(Object object, Serializable id) throws HibernateException {
        delegate.load(object, id);
    }

    @Override
    public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
        delegate.replicate(object, replicationMode);
    }

    @Override
    public void replicate(String entityName, Object object, ReplicationMode replicationMode) throws HibernateException {
        delegate.replicate(entityName, object, replicationMode);
    }

    @Override
    public Serializable save(Object object) throws HibernateException {
        return delegate.save(object);
    }

    @Override
    public Serializable save(String entityName, Object object) throws HibernateException {
        return delegate.save(entityName, object);
    }

    @Override
    public void saveOrUpdate(Object object) throws HibernateException {
        delegate.saveOrUpdate(object);
    }

    @Override
    public void saveOrUpdate(String entityName, Object object) throws HibernateException {
        delegate.saveOrUpdate(entityName, object);
    }

    @Override
    public void update(Object object) throws HibernateException {
        delegate.update(object);
    }

    @Override
    public void update(String entityName, Object object) throws HibernateException {
        delegate.update(entityName, object);
    }

    @Override
    public Object merge(Object object) throws HibernateException {
        return delegate.merge(object);
    }

    @Override
    public Object merge(String entityName, Object object) throws HibernateException {
        return delegate.merge(entityName, object);
    }

    @Override
    public void persist(Object object) throws HibernateException {
        delegate.persist(object);
    }

    @Override
    public void persist(String entityName, Object object) throws HibernateException {
        delegate.persist(entityName, object);
    }

    @Override
    public void delete(Object object) throws HibernateException {
        delegate.delete(object);
    }

    @Override
    public void delete(String entityName, Object object) throws HibernateException {
        delegate.delete(entityName, object);
    }

    @Override
    public void lock(Object object, LockMode lockMode) throws HibernateException {
        delegate.lock(object, lockMode);
    }

    @Override
    public void lock(String entityName, Object object, LockMode lockMode) throws HibernateException {
        delegate.lock(entityName, object, lockMode);
    }

    @Override
    public LockRequest buildLockRequest(LockOptions lockOptions) {
        return delegate.buildLockRequest(lockOptions);
    }

    @Override
    public void refresh(Object object) throws HibernateException {
        delegate.refresh(object);
    }

    @Override
    public void refresh(Object object, LockMode lockMode) throws HibernateException {
        delegate.refresh(object, lockMode);
    }

    @Override
    public void refresh(Object object, LockOptions lockOptions) throws HibernateException {
        delegate.refresh(object, lockOptions);
    }

    @Override
    public LockMode getCurrentLockMode(Object object) throws HibernateException {
        return delegate.getCurrentLockMode(object);
    }

    @Override
    public Transaction beginTransaction() throws HibernateException {
        return delegate.beginTransaction();
    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }

    @Override
    public Criteria createCriteria(Class persistentClass) {
        return delegate.createCriteria(persistentClass);
    }

    @Override
    public Criteria createCriteria(Class persistentClass, String alias) {
        return delegate.createCriteria(persistentClass, alias);
    }

    @Override
    public Criteria createCriteria(String entityName) {
        return delegate.createCriteria(entityName);
    }

    @Override
    public Criteria createCriteria(String entityName, String alias) {
        return delegate.createCriteria(entityName, alias);
    }

    @Override
    public Query createQuery(String queryString) throws HibernateException {
        return delegate.createQuery(queryString);
    }

    @Override
    public SQLQuery createSQLQuery(String queryString) throws HibernateException {
        return delegate.createSQLQuery(queryString);
    }

    @Override
    public Query createFilter(Object collection, String queryString) throws HibernateException {
        return delegate.createFilter(collection, queryString);
    }

    @Override
    public Query getNamedQuery(String queryName) throws HibernateException {
        return delegate.getNamedQuery(queryName);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Object get(Class clazz, Serializable id) throws HibernateException {
        return delegate.get(clazz, id);
    }

    @Override
    public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
        return delegate.get(clazz, id, lockMode);
    }

    @Override
    public Object get(Class clazz, Serializable id, LockOptions lockOptions) throws HibernateException {
        return delegate.get(clazz, id, lockOptions);
    }

    @Override
    public Object get(String entityName, Serializable id) throws HibernateException {
        return delegate.get(entityName, id);
    }

    @Override
    public Object get(String entityName, Serializable id, LockMode lockMode) throws HibernateException {
        return delegate.get(entityName, id, lockMode);
    }

    @Override
    public Object get(String entityName, Serializable id, LockOptions lockOptions) throws HibernateException {
        return delegate.get(entityName, id, lockOptions);
    }

    @Override
    public String getEntityName(Object object) throws HibernateException {
        return delegate.getEntityName(object);
    }

    @Override
    public Filter enableFilter(String filterName) {
        return delegate.enableFilter(filterName);
    }

    @Override
    public Filter getEnabledFilter(String filterName) {
        return delegate.getEnabledFilter(filterName);
    }

    @Override
    public void disableFilter(String filterName) {
        delegate.disableFilter(filterName);
    }

    @Override
    public SessionStatistics getStatistics() {
        return delegate.getStatistics();
    }

    @Override
    public boolean isReadOnly(Object entityOrProxy) {
        return delegate.isReadOnly(entityOrProxy);
    }

    @Override
    public void setReadOnly(Object entityOrProxy, boolean readOnly) {
        delegate.setReadOnly(entityOrProxy, readOnly);
    }

    @Override
    public void doWork(Work work) throws HibernateException {
        delegate.doWork(work);
    }

    @Override
    public Connection disconnect() throws HibernateException {
        return delegate.disconnect();
    }

    @Override
    public void reconnect() throws HibernateException {
        delegate.reconnect();
    }

    @Override
    public void reconnect(Connection connection) throws HibernateException {
        delegate.reconnect(connection);
    }

    @Override
    public boolean isFetchProfileEnabled(String name) throws UnknownProfileException {
        return delegate.isFetchProfileEnabled(name);
    }

    @Override
    public void enableFetchProfile(String name) throws UnknownProfileException {
        delegate.enableFetchProfile(name);
    }

    @Override
    public void disableFetchProfile(String name) throws UnknownProfileException {
        delegate.disableFetchProfile(name);
    }

    @Override
    public TypeHelper getTypeHelper() {
        return delegate.getTypeHelper();
    }

    @Override
    public LobHelper getLobHelper() {
        return delegate.getLobHelper();
    }
}
