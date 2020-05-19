/*
 * Copyright (c) 2020 Branden(r) Lda. All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r) Lda, Portuguese registered company.
 * Last modified 19/05/20, 15:28.
 */

package pt.branden.brandenportal.jhipsterframework.config.cache

import org.apache.commons.lang3.ObjectUtils
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.cache.interceptor.KeyGenerator
import java.lang.reflect.Method
import java.time.Instant
import java.time.format.DateTimeFormatter


const val RANDOM_SIZE = 12

class PrefixedKeyGenerator(
    gitProperties: GitProperties?,
    buildProperties: BuildProperties?
) : KeyGenerator {
    private val prefix = generatePrefix(gitProperties, buildProperties)

    private fun generatePrefix(gitProperties: GitProperties?, buildProperties: BuildProperties?): String {
        val shortCommitId = gitProperties?.shortCommitId

        val time = buildProperties?.time
        val version = buildProperties?.version

        val p: Any =
            ObjectUtils.firstNonNull(shortCommitId, time, version, RandomStringUtils.randomAlphanumeric(RANDOM_SIZE))!!

        return if (p is Instant) {
            DateTimeFormatter.ISO_INSTANT.format(p)
        } else p.toString()
    }

    override fun generate(target: Any, method: Method, vararg params: Any) =
        PrefixedSimpleKey(prefix, method.name, params)
}
