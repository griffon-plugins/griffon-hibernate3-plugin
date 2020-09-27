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
package griffon.plugins.hibernate3

import griffon.annotations.inject.BindTo
import griffon.core.GriffonApplication
import griffon.plugins.datasource.events.DataSourceConnectEndEvent
import griffon.plugins.datasource.events.DataSourceConnectStartEvent
import griffon.plugins.datasource.events.DataSourceDisconnectEndEvent
import griffon.plugins.datasource.events.DataSourceDisconnectStartEvent
import griffon.plugins.hibernate3.events.Hibernate3ConfigurationAvailableEvent
import griffon.plugins.hibernate3.events.Hibernate3ConnectEndEvent
import griffon.plugins.hibernate3.events.Hibernate3ConnectStartEvent
import griffon.plugins.hibernate3.events.Hibernate3DisconnectEndEvent
import griffon.plugins.hibernate3.events.Hibernate3DisconnectStartEvent
import griffon.plugins.hibernate3.exceptions.RuntimeHibernate3Exception
import griffon.test.core.GriffonUnitRule
import org.hibernate.Session
import org.junit.Rule
import spock.lang.Specification
import spock.lang.Unroll

import javax.application.event.EventHandler
import javax.inject.Inject

@Unroll
class Hibernate3Spec extends Specification {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    @Inject
    private Hibernate3Handler hibernate3Handler

    @Inject
    private GriffonApplication application

    void 'Open and close default hibernate3'() {
        given:
        List eventNames = [
            'Hibernate3ConnectStartEvent', 'DataSourceConnectStartEvent',
            'DataSourceConnectEndEvent', 'Hibernate3ConfigurationAvailableEvent', 'Hibernate3ConnectEndEvent',
            'Hibernate3DisconnectStartEvent', 'DataSourceDisconnectStartEvent',
            'DataSourceDisconnectEndEvent', 'Hibernate3DisconnectEndEvent'
        ]
        TestEventHandler testEventHandler = new TestEventHandler()
        application.eventRouter.subscribe(testEventHandler)

        when:
        hibernate3Handler.withHbm3Session { String sessionFactoryName, Session session ->
            true
        }
        hibernate3Handler.closeHbm3Session()
        // second call should be a NOOP
        hibernate3Handler.closeHbm3Session()

        then:
        testEventHandler.events.size() == 9
        testEventHandler.events == eventNames
    }

    void 'Connect to default SessionFactory'() {
        expect:
        hibernate3Handler.withHbm3Session { String sessionFactoryName, Session session ->
            sessionFactoryName == 'default' && session
        }
    }

    void 'Bootstrap init is called'() {
        given:
        assert !bootstrap.initWitness

        when:
        hibernate3Handler.withHbm3Session { String sessionFactoryName, Session session -> }

        then:
        bootstrap.initWitness
        !bootstrap.destroyWitness
    }

    void 'Bootstrap destroy is called'() {
        given:
        assert !bootstrap.initWitness
        assert !bootstrap.destroyWitness

        when:
        hibernate3Handler.withHbm3Session { String sessionFactoryName, Session session -> }
        hibernate3Handler.closeHbm3Session()

        then:
        bootstrap.initWitness
        bootstrap.destroyWitness
    }

    void 'Can connect to #name SessionFactory'() {
        expect:
        hibernate3Handler.withHbm3Session(name) { String sessionFactoryName, Session session ->
            sessionFactoryName == name && session
        }

        where:
        name       | _
        'default'  | _
        'internal' | _
        'people'   | _
    }

    void 'Bogus SessionFactory name (#name) results in error'() {
        when:
        hibernate3Handler.withHbm3Session(name) { String sessionFactoryName, Session session ->
            true
        }

        then:
        thrown(IllegalArgumentException)

        where:
        name    | _
        null    | _
        ''      | _
        'bogus' | _
    }

    void 'Execute statements on people table'() {
        when:
        List peopleIn = hibernate3Handler.withHbm3Session() { String sessionFactoryName, Session session ->
            [[id: 1, name: 'Danno', lastname: 'Ferrin'],
             [id: 2, name: 'Andres', lastname: 'Almiray'],
             [id: 3, name: 'James', lastname: 'Williams'],
             [id: 4, name: 'Guillaume', lastname: 'Laforge'],
             [id: 5, name: 'Jim', lastname: 'Shingler'],
             [id: 6, name: 'Alexander', lastname: 'Klein'],
             [id: 7, name: 'Rene', lastname: 'Groeschke']].each { data ->
                session.save(new Person(data))
            }
        }

        List peopleOut = hibernate3Handler.withHbm3Session() { String sessionFactoryName, Session session ->
            session.createQuery('from Person').list()*.asMap()
        }

        then:
        peopleIn == peopleOut
    }

    void 'A runtime exception is thrown within session handling'() {
        when:
        hibernate3Handler.withHbm3Session { String sessionFactoryName, Session session ->
            session.save(new Person())
        }

        then:
        thrown(RuntimeHibernate3Exception)
    }

    @BindTo(Hibernate3Bootstrap)
    private TestHibernate3Bootstrap bootstrap = new TestHibernate3Bootstrap()

    private class TestEventHandler {
        List<String> events = []

        @EventHandler
        void handleDataSourceConnectStartEvent(DataSourceConnectStartEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleDataSourceConnectEndEvent(DataSourceConnectEndEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleDataSourceDisconnectStartEvent(DataSourceDisconnectStartEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleDataSourceDisconnectEndEvent(DataSourceDisconnectEndEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleHibernate3ConnectStartEvent(Hibernate3ConnectStartEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleHibernate3ConfigurationAvailableEvent(Hibernate3ConfigurationAvailableEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleHibernate3ConnectEndEvent(Hibernate3ConnectEndEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleHibernate3DisconnectStartEvent(Hibernate3DisconnectStartEvent event) {
            events << event.class.simpleName
        }

        @EventHandler
        void handleHibernate3DisconnectEndEvent(Hibernate3DisconnectEndEvent event) {
            events << event.class.simpleName
        }
    }
}
