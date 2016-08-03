package tenant.test

import org.grails.datastore.mapping.multitenancy.exceptions.TenantNotFoundException
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver

/**
 * Like SystemPropertyTenantResolver but returns a Long tenant identifier.
 */
class TestTenantResolver extends SystemPropertyTenantResolver {

    static void setTenant(Long id) {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, String.valueOf(id ?: 0))
    }

    @Override
    Serializable resolveTenantIdentifier() throws TenantNotFoundException {
        Long.valueOf(super.resolveTenantIdentifier() ?: 0)
    }
}
