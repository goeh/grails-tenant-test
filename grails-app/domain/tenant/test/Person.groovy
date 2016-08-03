package tenant.test

/**
 * Multi-tenant entity.
 */
class Person extends MyTenantEntity<Person> {

    String firstName
    String lastName
}
