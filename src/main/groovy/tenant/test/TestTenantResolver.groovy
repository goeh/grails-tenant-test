package tenant.test

import groovy.transform.CompileStatic
import org.grails.datastore.mapping.multitenancy.exceptions.TenantNotFoundException
import org.grails.datastore.mapping.multitenancy.resolvers.SystemPropertyTenantResolver

/**
 * Like SystemPropertyTenantResolver but returns a Long tenant identifier.
 */
@CompileStatic
class TestTenantResolver extends SystemPropertyTenantResolver {

    static void setTenant(Long id) {
        System.setProperty(SystemPropertyTenantResolver.PROPERTY_NAME, String.valueOf(id ?: 0))
    }

    @Override
    Serializable resolveTenantIdentifier() throws TenantNotFoundException {
        Long.valueOf(super.resolveTenantIdentifier().toString())
    }
}
