/*
 * Copyright (c) 2020 Branden(r) Lda. All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r) Lda, Portuguese registered company.
 * Last modified 19/05/20, 15:29.
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
    private val params : Array<Any> = elements.copyOf() as Array<Any>
    private var hashCode: Int = calculateHash()

    override fun equals(other: Any?): Boolean {
        return this === other ||
            other is PrefixedSimpleKey && prefix == other.prefix && methodName == other.methodName &&
            params.contentDeepEquals(other.params)
    }

    override fun hashCode(): Int = hashCode

    override fun toString() =
        prefix + " " + javaClass.simpleName + methodName +
        " [" + StringUtils.arrayToCommaDelimitedString(params) + "]"

    private fun calculateHash(): Int =
        HASH_NUMBER * (HASH_NUMBER * prefix.hashCode() + methodName.hashCode()) + params.contentDeepHashCode()

}
