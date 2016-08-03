package tenant.test

import grails.gorm.MultiTenant

/**
 * Base trait for multi-tenant entities.
 */
abstract class MyTenantEntity<T> implements MultiTenant<T> {
    String tenantId
}
