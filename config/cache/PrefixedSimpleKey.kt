/*
 * Copyright 2016-2020 the original author or authors from the JHipster project.
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

package pt.branden.brandenportal.jhipsterframework.config.cache

import org.springframework.util.StringUtils
import java.io.Serializable

const val HASH_NUMBER = 31

class PrefixedSimpleKey(
    private val prefix: String,
    private val methodName: String,
    vararg elements: Any
) : Serializable {
    private val params = elements.copyOf()
    private var hashCode = calculateHash()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PrefixedSimpleKey

        if (prefix != other.prefix) return false
        if (methodName == other.methodName) return false
        if (!params.contentDeepEquals(other.params)) return false

        return true
    }

    override fun hashCode(): Int = hashCode

    override fun toString() =
        prefix + " " + javaClass.simpleName + methodName +
        " [" + StringUtils.arrayToCommaDelimitedString(params) + "]"

    private fun calculateHash(): Int =
        HASH_NUMBER * (HASH_NUMBER * prefix.hashCode() + methodName.hashCode()) + params.contentDeepHashCode()
}
