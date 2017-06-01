/*
 * Copyright 2014-2017 the original author or authors.
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
package org.codehaus.griffon.runtime.hibernate3.internal;

import griffon.core.GriffonApplication;
import griffon.util.GriffonNameUtils;
import org.codehaus.griffon.runtime.hibernate3.internal.exceptions.DatabaseException;
import org.hibernate.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hibernate.dialect.resolver.DialectFactory.buildDialect;

/**
 * @author Andres Almiray
 */
public class DialectDetector {
    private final DataSource dataSource;
    private final GriffonApplication application;

    public DialectDetector(GriffonApplication application, DataSource dataSource) {
        this.application = application;
        this.dataSource = dataSource;
    }

    public String getDialect() {
        Connection connection = null;

        try {
            String dbName = (String) JdbcUtils.extractDatabaseMetaData(dataSource, "getDatabaseProductName");
            connection = dataSource.getConnection();

            Dialect hibernateDialect = buildDialect(application.getConfiguration().asProperties(), connection);
            String hibernateDialectClassName = hibernateDialect.getClass().getName();

            if (GriffonNameUtils.isBlank(hibernateDialectClassName)) {
                throw new DatabaseException(
                    "Could not determine Hibernate dialect for database name [" + dbName + "]!");
            }

            return hibernateDialectClassName;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            JdbcUtils.closeConnection(connection);
        }
    }
}
