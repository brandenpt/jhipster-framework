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
