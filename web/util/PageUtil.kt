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

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import kotlin.math.min

/**
 * Create a [org.springframework.data.domain.Page] from a [java.util.List] of objects
 *
 * @param list list of objects
 * @param pageable pagination information.
 * @param T type of object
 * @return page containing objects, and attributes set according to pageable
 * @throws java.lang.IllegalArgumentException - if list is null
 */
fun <T> createPageFromList(list: List<T>, pageable: Pageable): Page<T> {
    val startOfPage: Int = pageable.pageNumber * pageable.pageSize
    if (startOfPage > list.size) {
        return PageImpl<T>(mutableListOf(), pageable, 0)
    }

    val endOfPage = min(startOfPage + pageable.pageSize, list.size)
    return PageImpl(list.subList(startOfPage, endOfPage), pageable, list.size.toLong())
}
