/*
 * Copyright 2016-2019 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.branden.brandenportal.jhipsterframework.web.util

import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Utility class for HTTP headers creation.
 */
object HeaderUtil {
    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * createAlert.
     *
     * @param applicationName a [java.lang.String] object.
     * @param message a [java.lang.String] object.
     * @param param a [java.lang.String] object.
     * @return a [org.springframework.http.HttpHeaders] object.
     */
    fun createAlert(applicationName: String, message: String, param: String): HttpHeaders {
        return HttpHeaders().apply {
            add("X-$applicationName-alert", message)
            add("X-$applicationName-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()))
        }
    }

    /**
     * createEntityCreationAlert.
     *
     * @param applicationName a [java.lang.String] object.
     * @param enableTranslation a boolean.
     * @param entityName a [java.lang.String] object.
     * @param param a [java.lang.String] object.
     * @return a [org.springframework.http.HttpHeaders] object.
     */
    fun createEntityCreationAlert(
        applicationName: String,
        enableTranslation: Boolean,
        entityName: String,
        param: String
    ): HttpHeaders {
        val message = if (enableTranslation) {
            "$applicationName.$entityName.created"
        } else {
            "A new $entityName is created with identifier $param"
        }

        return createAlert(applicationName, message, param)
    }

    /**
     * createEntityUpdateAlert.
     *
     * @param applicationName a [java.lang.String] object.
     * @param enableTranslation a boolean.
     * @param entityName a [java.lang.String] object.
     * @param param a [java.lang.String] object.
     * @return a [org.springframework.http.HttpHeaders] object.
     */
    fun createEntityUpdateAlert(
        applicationName: String,
        enableTranslation: Boolean,
        entityName: String,
        param: String
    ): HttpHeaders {
        val message = if (enableTranslation) {
            "$applicationName.$entityName.updated"
        } else {
            "A $entityName is updated with identifier $param"
        }

        return createAlert(applicationName, message, param)
    }

    /**
     * createEntityDeletionAlert.
     *
     * @param applicationName a [java.lang.String] object.
     * @param enableTranslation a boolean.
     * @param entityName a [java.lang.String] object.
     * @param param a [java.lang.String] object.
     * @return a [org.springframework.http.HttpHeaders] object.
     */
    fun createEntityDeletionAlert(
        applicationName: String,
        enableTranslation: Boolean,
        entityName: String,
        param: String
    ): HttpHeaders {
        val message = if (enableTranslation) {
            "$applicationName.$entityName.deleted"
        } else {
            "A $entityName is deleted with identifier $param"
        }

        return createAlert(applicationName, message, param)
    }

    /**
     *
     * createFailureAlert.
     *
     * @param applicationName a [java.lang.String] object.
     * @param enableTranslation a boolean.
     * @param entityName a [java.lang.String] object.
     * @param errorKey a [java.lang.String] object.
     * @param defaultMessage a [java.lang.String] object.
     * @return a [org.springframework.http.HttpHeaders] object.
     */
    fun createFailureAlert(
        applicationName: String,
        enableTranslation: Boolean,
        entityName: String,
        errorKey: String,
        defaultMessage: String?
    ): HttpHeaders {
        log.error("Entity processing failed, {}", defaultMessage)

        val message = if (enableTranslation) "error.$errorKey" else defaultMessage
        return HttpHeaders().apply {
            add("X-$applicationName-error", message)
            add("X-$applicationName-params", entityName)
        }
    }
}
