/*
 * Copyright (c) 2019 Branden(r). All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r), Portuguese registered company.
 * Last modified 27/04/20, 22:41.
 */

package pt.branden.brandenportal.jhipsterframework.domain.util

import org.hibernate.dialect.PostgreSQL10Dialect
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor
import java.sql.Types

/**
 * FixedPostgreSQL10Dialect class.
 */
class FixedPostgreSQL10Dialect : PostgreSQL10Dialect() {
    init {
        registerColumnType(Types.BLOB, "bytea")
    }

    override fun remapSqlTypeDescriptor(sqlTypeDescriptor: SqlTypeDescriptor): SqlTypeDescriptor {
        return if (sqlTypeDescriptor.sqlType == Types.BLOB) {
            BinaryTypeDescriptor.INSTANCE
        } else super.remapSqlTypeDescriptor(sqlTypeDescriptor)
    }
}
