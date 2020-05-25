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

package pt.branden.brandenportal.jhipsterframework.web.util

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ResponseStatusException

/**
 * Wrap the optional into a [org.springframework.http.ResponseEntity] with an
 * [org.springframework.http.HttpStatus.OK] status, or if it's empty, it
 * returns a [org.springframework.http.ResponseEntity] with [org.springframework.http.HttpStatus.NOT_FOUND].
 *
 * @param X type of the response
 * @param maybeResponse response to return if present
 * @return response containing `maybeResponse` if present or [org.springframework.http.HttpStatus.NOT_FOUND]
 */
fun <X> wrapOrNotFound(maybeResponse: X?): ResponseEntity<X> {
    return wrapOrNotFound(maybeResponse, null)
}

/**
 * Wrap the optional into a [org.springframework.http.ResponseEntity] with an [org.springframework.http.HttpStatus.OK]
 * status with the headers, or if it's empty, it returns a [org.springframework.web.server.ResponseStatusException]
 * with [org.springframework.http.HttpStatus.NOT_FOUND].
 *
 * @param X type of the response
 * @param maybeResponse response to return if present
 * @param header headers to be added to the response
 * @return response containing `maybeResponse` if present
 * @throws ResponseStatusException `404 (Not found)` if `maybeResponse` is empty
 */
fun <X> wrapOrNotFound(maybeResponse: X?, header: HttpHeaders?): ResponseEntity<X> {
    return maybeResponse?.let { ResponseEntity.ok().headers(header).body(it) }
        ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
}
