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

import liquibase.exception.LiquibaseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.liquibase.DataSourceClosingSpringLiquibase
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.util.StopWatch
import io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_DEVELOPMENT
import io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_HEROKU
import io.github.jhipster.config.JHipsterConstants.SPRING_PROFILE_NO_LIQUIBASE
import java.sql.SQLException
import java.util.concurrent.Executor

/**
 * Specific liquibase.integration.spring.SpringLiquibase that will update the database asynchronously and close
 * DataSource if necessary. <p> By default, this asynchronous version only works when using the "dev" profile.<p>
 * The standard liquibase.integration.spring.SpringLiquibase starts Liquibase in the current thread: <ul>
 * <li>This is needed if you want to do some database requests at startup</li>
 * <li>This ensure that the database is ready when the application starts</li> </ul>
 * But as this is a rather slow process, we use this asynchronous version to speed up our start-up time:
 * <ul><li>On a recent MacBook Pro, start-up time is down from 14 seconds to 8 seconds</li>
 * <li>In production, this can help your application run on platforms like Heroku,
 * where it must start/restart very quickly</li> </ul>
 *
 * @param executor a {@link java.util.concurrent.Executor} object.
 * @param env a {@link org.springframework.core.env.Environment} object.
 */
class AsyncSpringLiquibase(
    private val executor: Executor,
    private val env: Environment
) : DataSourceClosingSpringLiquibase() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Throws(LiquibaseException::class)
    override fun afterPropertiesSet() {
        if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_NO_LIQUIBASE))) {
            logger.debug(DISABLED_MESSAGE)
            return
        }

        if (!env.acceptsProfiles(Profiles.of("$SPRING_PROFILE_DEVELOPMENT|$SPRING_PROFILE_HEROKU"))) {
            logger.debug(STARTING_SYNC_MESSAGE)
            initDb()
            return
        }

        // Prevent Thread Lock with spring-cloud-context GenericScope
        // https://github.com/spring-cloud/spring-cloud-commons/commit/aaa7288bae3bb4d6fdbef1041691223238d77b7b#diff-afa0715eafc2b0154475fe672dab70e4R328
        try {
            getDataSource().connection.use {
                executor.execute {
                    try {
                        logger.warn(STARTING_ASYNC_MESSAGE)
                        initDb()
                    } catch (e: LiquibaseException) {
                        logger.error(EXCEPTION_MESSAGE, e.message, e)
                    }
                }
            }
        } catch (e: SQLException) {
            logger.error(EXCEPTION_MESSAGE, e.message, e)
        }
    }

    /**
     *
     * initDb.
     *
     * @throws liquibase.exception.LiquibaseException if any.
     */
    @Throws(LiquibaseException::class)
    private fun initDb() {
        val watch = StopWatch()
        watch.start()
        super.afterPropertiesSet()
        watch.stop()
        logger.debug(STARTED_MESSAGE, watch.totalTimeMillis)
        if (watch.totalTimeMillis > SLOWNESS_THRESHOLD * SECOND_SIZE) {
            logger.warn(SLOWNESS_MESSAGE, SLOWNESS_THRESHOLD)
        }
    }

    companion object {
        /** Constant `DISABLED_MESSAGE="Liquibase is disabled"`  */
        const val DISABLED_MESSAGE = "Liquibase is disabled"
        /** Constant `STARTING_ASYNC_MESSAGE="Starting Liquibase asynchronously, your"{trunked}`  */
        const val STARTING_ASYNC_MESSAGE =
            "Starting Liquibase asynchronously, your database might not be ready at startup!"
        /** Constant `STARTING_SYNC_MESSAGE="Starting Liquibase synchronously"`  */
        const val STARTING_SYNC_MESSAGE = "Starting Liquibase synchronously"
        /** Constant `STARTED_MESSAGE="Liquibase has updated your database in "{trunked}`  */
        const val STARTED_MESSAGE = "Liquibase has updated your database in {} ms"
        /** Constant `EXCEPTION_MESSAGE="Liquibase could not start correctly, yo"{trunked}`  */
        const val EXCEPTION_MESSAGE = "Liquibase could not start correctly, your database is NOT ready: {}"

        /** Constant `SLOWNESS_THRESHOLD=5`  */
        const val SLOWNESS_THRESHOLD = 5L // seconds
        const val SECOND_SIZE = 1000L

        /** Constant `SLOWNESS_MESSAGE="Warning, Liquibase took more than {} se"{trunked}`  */
        const val SLOWNESS_MESSAGE = "Warning, Liquibase took more than {} seconds to start up!"
    }
}
