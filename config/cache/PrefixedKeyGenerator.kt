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
