/*
 * Copyright (c) 2019 Branden(r). All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r), Portuguese registered company.
 * Last modified 16/11/19, 21:44.
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
