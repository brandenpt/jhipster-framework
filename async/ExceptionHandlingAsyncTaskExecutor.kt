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

package pt.branden.brandenportal.jhipsterframework.async

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.task.AsyncTaskExecutor
import java.util.concurrent.Callable
import java.util.concurrent.Future

/**
 * <p>ExceptionHandlingAsyncTaskExecutor class.</p>
 *
 * @param executor a {@link org.springframework.core.task.AsyncTaskExecutor} object.
 */
class ExceptionHandlingAsyncTaskExecutor(
    private val executor: AsyncTaskExecutor
) : AsyncTaskExecutor, InitializingBean, DisposableBean {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun execute(task: Runnable) {
        executor.execute(createWrappedRunnable(task))
    }

    override fun execute(task: Runnable, startTimeout: Long) {
        executor.execute(createWrappedRunnable(task), startTimeout)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun <T> createCallable(task: Callable<T>): Callable<T> {
        return Callable {
            try {
                task.call()
            } catch (e: Exception) {
                handle(e)
                throw e
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    private fun createWrappedRunnable(task: Runnable): Runnable {
        return Runnable {
            try {
                task.run()
            } catch (e: Exception) {
                handle(e)
            }
        }
    }

    /**
     *
     * handle.
     *
     * @param e a [java.lang.Exception] object.
     */
    private fun handle(e: Exception) {
        log.error(EXCEPTION_MESSAGE, e)
    }

    override fun submit(task: Runnable): Future<*> {
        return executor.submit(createWrappedRunnable(task))
    }

    override fun <T> submit(task: Callable<T>): Future<T> {
        return executor.submit(createCallable(task))
    }

    @Throws(java.lang.Exception::class)
    override fun destroy() {
        (executor as? DisposableBean)?.destroy()
    }

    @Throws(java.lang.Exception::class)
    override fun afterPropertiesSet() {
        (executor as? InitializingBean)?.afterPropertiesSet()
    }

    companion object {
        const val EXCEPTION_MESSAGE = "Caught async exception"
    }
}
