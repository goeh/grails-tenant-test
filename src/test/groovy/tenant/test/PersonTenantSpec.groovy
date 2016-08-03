package tenant.test

import org.grails.datastore.mapping.core.DatastoreUtils
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver
import org.grails.orm.hibernate.HibernateDatastore
import org.hibernate.Session
import org.hibernate.dialect.H2Dialect
import org.springframework.orm.hibernate5.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import static grails.gorm.multitenancy.Tenants.withId
import static grails.gorm.multitenancy.Tenants.withoutId

/**
 * Tenant test.
 */
class PersonTenantSpec extends Specification {

    @Shared
    @AutoCleanup
    HibernateDatastore datastore

    void setupSpec() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
        Map config = [
                "grails.gorm.multiTenancy.mode"               : "DISCRIMINATOR",
                "grails.gorm.multiTenancy.tenantResolverClass": SystemPropertyTenantResolver,
                'dataSource.url'                              : "jdbc:h2:mem:grailsDB;MVCC=TRUE;LOCK_TIMEOUT=10000",
                'dataSource.dbCreate'                         : 'update',
                'dataSource.dialect'                          : H2Dialect.name,
                'dataSource.formatSql'                        : 'true',
                'hibernate.flush.mode'                        : 'COMMIT',
                'hibernate.cache.queries'                     : 'true',
                'hibernate.hbm2ddl.auto'                      : 'create'
        ]

        datastore = new HibernateDatastore(DatastoreUtils.createPropertyResolver(config), Person)
    }

    Session session

    void setup() {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "")
        def sessionFactory = datastore.sessionFactory
        session = sessionFactory.openSession()
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
    }

    void cleanup() {
        def sessionFactory = datastore.sessionFactory
        session.close()
        TransactionSynchronizationManager.unbindResource(sessionFactory)
    }

    void "save person"() {
        when:
        session.clear()
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "one")

        then:
        new Person(firstName: "Fred", lastName: "Flintstone").save(flush: true)

        when:
        session.clear()
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, "two")

        then:
        new Person(firstName: "Wilma", lastName: "Flintstone").save(flush: true)

        and:
        withId("one") { Person.list().size() } == 1
        withId("two") { Person.list().size() } == 1
        withoutId { Person.list().size() } == 2

    }
}
