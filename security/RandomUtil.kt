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

package pt.branden.brandenportal.jhipsterframework.security

//noinspection TrulyRandom
import org.apache.commons.lang3.RandomStringUtils
//noinspection TrulyRandom
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
