/*
 * Copyright 2016-2019 the original author or authors from the JHipster project.
 *
 * Modifications Copyright (c) 2020 Branden(r) Lda. Developed by Fernando Rodrigues.
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

import java.text.MessageFormat
import org.springframework.data.domain.Page
import org.springframework.http.HttpHeaders
import org.springframework.web.util.UriComponentsBuilder

/**
 * Utility class for handling pagination.
 *
 *
 *
 * Pagination uses the same principles as the [GitHub API](https://developer.github.com/v3/#pagination),
 * and follow [RFC 5988 (Link header)](http://tools.ietf.org/html/rfc5988).
 */
object PaginationUtil {
    private const val HEADER_X_TOTAL_COUNT = "X-Total-Count"
    private const val HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\""

    /**
     * Generate pagination headers for a Spring Data [org.springframework.data.domain.Page] object.
     *
     * @param uriBuilder The URI builder.
     * @param page The page.
     * @param T The type of object.
     * @return http header.
     */
    fun <T> generatePaginationHttpHeaders(
        uriBuilder: UriComponentsBuilder,
        page: Page<T>
    ): HttpHeaders {
        val headers = HttpHeaders().apply {
            add(HEADER_X_TOTAL_COUNT, "${page.totalElements}")
        }
        val pageNumber = page.number
        val pageSize = page.size
        val link = StringBuilder()
        if (pageNumber < page.totalPages - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next"))
                .append(",")
        }
        if (pageNumber > 0) {
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev"))
                .append(",")
        }
        link.append(prepareLink(uriBuilder, page.totalPages - 1, pageSize, "last"))
            .append(",")
            .append(prepareLink(uriBuilder, 0, pageSize, "first"))
        headers.add(HttpHeaders.LINK, link.toString())
        return headers
    }

    private fun prepareLink(
        uriBuilder: UriComponentsBuilder,
        pageNumber: Int,
        pageSize: Int,
        relType: String
    ): String {
        return MessageFormat.format(
            HEADER_LINK_FORMAT,
            preparePageUri(uriBuilder, pageNumber, pageSize),
            relType
        )
    }

    private fun preparePageUri(
        uriBuilder: UriComponentsBuilder,
        pageNumber: Int,
        pageSize: Int
    ): String {
        return uriBuilder.replaceQueryParam("page", "$pageNumber")
            .replaceQueryParam("size", "$pageSize")
            .toUriString()
            .replace(",", "%2C")
            .replace(";", "%3B")
    }
}
