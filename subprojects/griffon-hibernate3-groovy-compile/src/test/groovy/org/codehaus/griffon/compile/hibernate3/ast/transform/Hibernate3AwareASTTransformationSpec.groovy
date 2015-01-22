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
package org.codehaus.griffon.compile.hibernate3.ast.transform

import griffon.plugins.hibernate3.Hibernate3Handler
import spock.lang.Specification

import java.lang.reflect.Method

/**
 * @author Andres Almiray
 */
class Hibernate3AwareASTTransformationSpec extends Specification {
    def 'Hibernate3AwareASTTransformation is applied to a bean via @Hibernate3Aware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        @griffon.transform.Hibernate3Aware
        class Bean { }
        new Bean()
        ''')

        then:
        bean instanceof Hibernate3Handler
        Hibernate3Handler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                candidate.returnType == target.returnType &&
                candidate.parameterTypes == target.parameterTypes &&
                candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }

    def 'Hibernate3AwareASTTransformation is not applied to a Hibernate3Handler subclass via @Hibernate3Aware'() {
        given:
        GroovyShell shell = new GroovyShell()

        when:
        def bean = shell.evaluate('''
        import griffon.plugins.hibernate3.Hibernate3Callback
        import griffon.plugins.hibernate3.exceptions.RuntimeHibernate3Exception
        import griffon.plugins.hibernate3.Hibernate3Handler

        import javax.annotation.Nonnull
        @griffon.transform.Hibernate3Aware
        class Hibernate3HandlerBean implements Hibernate3Handler {
            @Override
            public <R> R withHbm3Session(@Nonnull Hibernate3Callback<R> callback) throws RuntimeHibernate3Exception {
                return null
            }
            @Override
            public <R> R withHbm3Session(@Nonnull String sessionFactoryName, @Nonnull Hibernate3Callback<R> callback) throws RuntimeHibernate3Exception {
                return null
            }
            @Override
            void closeHbm3Session(){}
            @Override
            void closeHbm3Session(@Nonnull String sessionFactoryName){}
        }
        new Hibernate3HandlerBean()
        ''')

        then:
        bean instanceof Hibernate3Handler
        Hibernate3Handler.methods.every { Method target ->
            bean.class.declaredMethods.find { Method candidate ->
                candidate.name == target.name &&
                    candidate.returnType == target.returnType &&
                    candidate.parameterTypes == target.parameterTypes &&
                    candidate.exceptionTypes == target.exceptionTypes
            }
        }
    }
}
