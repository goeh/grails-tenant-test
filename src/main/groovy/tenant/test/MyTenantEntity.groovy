package tenant.test

import grails.gorm.MultiTenant

/**
 * Base trait for multi-tenant entities.
 */
trait MyTenantEntity<T> extends MultiTenant<T> {
    String tenantId = 'none'
}
