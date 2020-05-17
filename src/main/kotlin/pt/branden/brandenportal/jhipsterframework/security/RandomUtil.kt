/*
 * Copyright (c) 2019 Branden(r). All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r), Portuguese registered company.
 * Last modified 22/04/20, 10:57.
 */

package pt.branden.brandenportal.jhipsterframework.security

import org.apache.commons.lang3.RandomStringUtils
import java.security.SecureRandom


/**
 * Utility class for generating random Strings.
 */
object RandomUtil {
    private const val DEF_COUNT = 20
    private const val BYTE_ARRAY_SIZE = 64
    private var SECURE_RANDOM = SecureRandom()

    private fun generateRandomAlphanumericString(): String {
        return RandomStringUtils.random(
            DEF_COUNT,
            0,
            0,
            true,
            true,
            null,
            SECURE_RANDOM
        )
    }

    /**
     * Generate a password.
     *
     * @return the generated password.
     */
    fun generatePassword(): String {
        return generateRandomAlphanumericString()
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key.
     */
    fun generateActivationKey(): String {
        return generateRandomAlphanumericString()
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key.
     */
    fun generateResetKey(): String {
        return generateRandomAlphanumericString()
    }

    init {
        SECURE_RANDOM.nextBytes(ByteArray(BYTE_ARRAY_SIZE))
    }
}
