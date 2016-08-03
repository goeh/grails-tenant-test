package tenant.test

import grails.gorm.MultiTenant

/**
 * Multi-tenant entity.
 */
class Person implements MyTenantEntity/*, MultiTenant*/<Person> {

    String firstName
    String lastName
}
