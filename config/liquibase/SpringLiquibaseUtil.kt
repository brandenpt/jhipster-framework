/*
 * Copyright 2016-2019 the original author or authors from the JHipster project.
 *
 * Modifications Copyright (c) 2020 Branden(r) Lda. Developed by Fernando Rodrigues.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.branden.brandenportal.jhipsterframework.config.liquibase

import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.core.env.Environment
import java.util.concurrent.Executor
import java.util.function.Supplier
import javax.sql.DataSource

@Suppress("MaxLineLength")
/**
 * Utility class for handling SpringLiquibase.
 *
 * <p>
 * It follows implementation of
 * <a href="https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration.java">LiquibaseAutoConfiguration</a>.
 */
object SpringLiquibaseUtil {

    @Suppress("unused")
    fun createSpringLiquibase(
        liquibaseDatasource: DataSource?,
        liquibaseProperties: LiquibaseProperties,
        dataSource: DataSource,
        dataSourceProperties: DataSourceProperties
    ): SpringLiquibase {

        getDataSource(liquibaseDatasource, liquibaseProperties, dataSource)?.let {
            return SpringLiquibase().apply { this.dataSource = it }
        }

        return DataSourceClosingSpringLiquibase().apply {
            setDataSource(createNewDataSource(liquibaseProperties, dataSourceProperties))
        }
    }

    @Suppress("LongParameterList")
    fun createAsyncSpringLiquibase(
        env: Environment,
        executor: Executor,
        liquibaseDatasource: DataSource?,
        liquibaseProperties: LiquibaseProperties?,
        dataSource: DataSource?,
        dataSourceProperties: DataSourceProperties
    ): SpringLiquibase {
        val liquibase: SpringLiquibase

        getDataSource(liquibaseDatasource, liquibaseProperties!!, dataSource)?.let {
            liquibase = AsyncSpringLiquibase(executor, env)
            liquibase.setCloseDataSourceOnceMigrated(false)
            liquibase.dataSource = it
            return liquibase
        }

        liquibase = AsyncSpringLiquibase(executor, env)
        liquibase.dataSource = createNewDataSource(liquibaseProperties, dataSourceProperties)
        return liquibase
    }

    private fun getDataSource(
        liquibaseDataSource: DataSource?,
        liquibaseProperties: LiquibaseProperties,
        dataSource: DataSource?
    ): DataSource? {
        liquibaseDataSource?.let { return liquibaseDataSource }
        return if (liquibaseProperties.url == null && liquibaseProperties.user == null) { dataSource } else null
    }

    private fun createNewDataSource(
        liquibaseProperties: LiquibaseProperties,
        dataSourceProperties: DataSourceProperties
    ): DataSource {
        val url: String =
            getProperty(Supplier { liquibaseProperties.url },
                Supplier { dataSourceProperties.determineUrl() })
        val user: String =
            getProperty(Supplier { liquibaseProperties.user },
                Supplier { dataSourceProperties.determineUsername() })
        val password: String =
            getProperty(Supplier { liquibaseProperties.password },
                Supplier { dataSourceProperties.determinePassword() })
        return DataSourceBuilder.create().url(url).username(user).password(password).build()
    }

    private fun getProperty(
        property: Supplier<String?>,
        defaultValue: Supplier<String?>
    ): String {
        return property.get() ?: defaultValue.get()!!
    }
}
