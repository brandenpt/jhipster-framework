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

package pt.branden.brandenportal.jhipsterframework.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.PropertySource
import org.springframework.context.annotation.PropertySources
import org.springframework.web.cors.CorsConfiguration

/**
 * Properties specific to JHipster.
 *
 *
 * Properties are configured in the application.yml file.
 *
 *
 * This class also load properties in the Spring Environment
 * from the `git.properties` and `META-INF/build-info.properties`
 * files if they are found in the classpath.
 */
@ConfigurationProperties(prefix = "jhipster", ignoreUnknownFields = false)
@PropertySources(
    PropertySource(value = ["classpath:git.properties"], ignoreResourceNotFound = true),
    PropertySource(value = ["classpath:META-INF/build-info.properties"], ignoreResourceNotFound = true)
)
data class JHipsterProperties(
    val async: Async = Async(),

    val http: Http = Http(),

    val cache: Cache = Cache(),

    val mail: Mail = Mail(),

    val security: Security = Security(),

    val swagger: Swagger = Swagger(),

    val metrics: Metrics = Metrics(),

    val logging: Logging = Logging(),

    val cors: CorsConfiguration = CorsConfiguration(),

    val social: Social = Social(),

    val gateway: Gateway = Gateway(),

    val registry: Registry = Registry(),

    val clientApp: ClientApp = ClientApp(),

    val auditEvents: AuditEvents = AuditEvents()
) {

    data class Async(
        var corePoolSize: Int = JHipsterDefaults.Async.corePoolSize,
        var maxPoolSize: Int = JHipsterDefaults.Async.maxPoolSize,
        var queueCapacity: Int = JHipsterDefaults.Async.queueCapacity
    )

    data class Http(
        val cache: Cache = Cache()
    ) {
        data class Cache(
            var timeToLiveInDays: Int = JHipsterDefaults.Http.Cache.timeToLiveInDays
        )
    }

    data class Cache(
        val hazelcast: Hazelcast = Hazelcast(),

        val caffeine: Caffeine = Caffeine(),

        val ehcache: Ehcache = Ehcache(),

        val infinispan: Infinispan = Infinispan(),

        val memcached: Memcached = Memcached(),

        val redis: Redis = Redis()
    ) {

        data class Hazelcast(
            var timeToLiveSeconds: Int = JHipsterDefaults.Cache.Hazelcast.timeToLiveSeconds,

            var backupCount: Int = JHipsterDefaults.Cache.Hazelcast.backupCount,

            val managementCenter: ManagementCenter = ManagementCenter()
        ) {
            data class ManagementCenter(
                var isEnabled: Boolean =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.enabled,
                var updateInterval: Int =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.updateInterval,
                var url: String =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.url
            )
        }

        data class Caffeine(
            var timeToLiveSeconds: Int = JHipsterDefaults.Cache.Caffeine.timeToLiveSeconds,

            var maxEntries: Long = JHipsterDefaults.Cache.Caffeine.maxEntries
        )

        data class Ehcache(
            var timeToLiveSeconds: Int = JHipsterDefaults.Cache.Ehcache.timeToLiveSeconds,

            var maxEntries: Long = JHipsterDefaults.Cache.Ehcache.maxEntries
        )

        data class Infinispan(
            var configFile: String = JHipsterDefaults.Cache.Infinispan.configFile,

            var isStatsEnabled: Boolean = JHipsterDefaults.Cache.Infinispan.statsEnabled,

            val local: Local = Local(),

            val distributed: Distributed = Distributed(),

            val replicated: Replicated = Replicated()
        ) {
            data class Local(
                var timeToLiveSeconds: Long = JHipsterDefaults.Cache.Infinispan.Local.timeToLiveSeconds,

                var maxEntries: Long = JHipsterDefaults.Cache.Infinispan.Local.maxEntries
            )

            data class Distributed(
                var timeToLiveSeconds: Long = JHipsterDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds,

                var maxEntries: Long = JHipsterDefaults.Cache.Infinispan.Distributed.maxEntries,

                var instanceCount: Int = JHipsterDefaults.Cache.Infinispan.Distributed.instanceCount
            )

            data class Replicated(
                var timeToLiveSeconds: Long = JHipsterDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds,

                var maxEntries: Long = JHipsterDefaults.Cache.Infinispan.Replicated.maxEntries
            )
        }

        data class Memcached(
            var isEnabled: Boolean = JHipsterDefaults.Cache.Memcached.enabled,

            /**
             * Comma or whitespace separated list of servers' addresses.
             */
            var servers: String = JHipsterDefaults.Cache.Memcached.servers,

            var expiration: Int = JHipsterDefaults.Cache.Memcached.expiration,

            var isUseBinaryProtocol: Boolean = JHipsterDefaults.Cache.Memcached.useBinaryProtocol
        )

        data class Redis(
            var server: String = JHipsterDefaults.Cache.Redis.server,

            var expiration: Int = JHipsterDefaults.Cache.Redis.expiration,

            var cluster: Boolean = JHipsterDefaults.Cache.Redis.cluster
        )
    }

    data class Mail(
        var isEnabled: Boolean = JHipsterDefaults.Mail.enabled,

        var from: String = JHipsterDefaults.Mail.from,

        var baseUrl: String = JHipsterDefaults.Mail.baseUrl
    )

    data class Security(
        val clientAuthorization: ClientAuthorization = ClientAuthorization(),

        val authentication: Authentication = Authentication(),

        val rememberMe: RememberMe = RememberMe(),

        val oauth2: OAuth2 = OAuth2()
    ) {

        data class ClientAuthorization(
            var accessTokenUri: String? = JHipsterDefaults.Security.ClientAuthorization.accessTokenUri,

            var tokenServiceId: String? = JHipsterDefaults.Security.ClientAuthorization.tokenServiceId,

            var clientId: String? = JHipsterDefaults.Security.ClientAuthorization.clientId,

            var clientSecret: String? = JHipsterDefaults.Security.ClientAuthorization.clientSecret
        )

        data class Authentication(
            val jwt: Jwt = Jwt()
        ) {

            data class Jwt(
                var secret: String? = JHipsterDefaults.Security.Authentication.Jwt.secret,

                var base64Secret: String? = JHipsterDefaults.Security.Authentication.Jwt.base64Secret,

                var tokenValidityInSeconds: Long = JHipsterDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds,

                var tokenValidityInSecondsForRememberMe: Long = JHipsterDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe
            )
        }

        data class RememberMe(
            var key: String? = JHipsterDefaults.Security.RememberMe.key
        )

        @Suppress("ConstructorParameterNaming")
        data class OAuth2(
            private var _audience: MutableList<String> = mutableListOf()
        ) {

            var audience: List<String>
                get() = _audience
                set(value) { _audience.addAll(value) }
        }
    }

    data class Swagger(
        var title: String = JHipsterDefaults.Swagger.title,

        var description: String = JHipsterDefaults.Swagger.description,

        var version: String = JHipsterDefaults.Swagger.version,

        var termsOfServiceUrl: String? = JHipsterDefaults.Swagger.termsOfServiceUrl,

        var contactName: String? = JHipsterDefaults.Swagger.contactName,

        var contactUrl: String? = JHipsterDefaults.Swagger.contactUrl,

        var contactEmail: String? = JHipsterDefaults.Swagger.contactEmail,

        var license: String? = JHipsterDefaults.Swagger.license,

        var licenseUrl: String? = JHipsterDefaults.Swagger.licenseUrl,

        var defaultIncludePattern: String = JHipsterDefaults.Swagger.defaultIncludePattern,

        var host: String? = JHipsterDefaults.Swagger.host,

        var protocols: Array<String> = JHipsterDefaults.Swagger.protocols,

        var isUseDefaultResponseMessages: Boolean = JHipsterDefaults.Swagger.useDefaultResponseMessages
    ) {

        @Suppress("ComplexMethod")
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Swagger

            if (title != other.title) return false
            if (description != other.description) return false
            if (version != other.version) return false
            if (termsOfServiceUrl != other.termsOfServiceUrl) return false
            if (contactName != other.contactName) return false
            if (contactUrl != other.contactUrl) return false
            if (contactEmail != other.contactEmail) return false
            if (license != other.license) return false
            if (licenseUrl != other.licenseUrl) return false
            if (defaultIncludePattern != other.defaultIncludePattern) return false
            if (host != other.host) return false
            if (!protocols.contentEquals(other.protocols)) return false
            if (isUseDefaultResponseMessages != other.isUseDefaultResponseMessages) return false

            return true
        }

        override fun hashCode(): Int {
            var result = title.hashCode()
            result = 31 * result + description.hashCode()
            result = 31 * result + version.hashCode()
            result = 31 * result + (termsOfServiceUrl?.hashCode() ?: 0)
            result = 31 * result + (contactName?.hashCode() ?: 0)
            result = 31 * result + (contactUrl?.hashCode() ?: 0)
            result = 31 * result + (contactEmail?.hashCode() ?: 0)
            result = 31 * result + (license?.hashCode() ?: 0)
            result = 31 * result + (licenseUrl?.hashCode() ?: 0)
            result = 31 * result + defaultIncludePattern.hashCode()
            result = 31 * result + (host?.hashCode() ?: 0)
            result = 31 * result + protocols.contentHashCode()
            result = 31 * result + isUseDefaultResponseMessages.hashCode()
            return result
        }
    }

    data class Metrics(
        val logs: Logs = Logs()
    ) {

        data class Logs(
            var isEnabled: Boolean = JHipsterDefaults.Metrics.Logs.enabled,

            var reportFrequency: Long = JHipsterDefaults.Metrics.Logs.reportFrequency
        )
    }

    data class Logging(
        var isUseJsonFormat: Boolean = JHipsterDefaults.Logging.useJsonFormat,

        val logstash: Logstash = Logstash()
    ) {


        data class Logstash(
            var isEnabled: Boolean = JHipsterDefaults.Logging.Logstash.enabled,

            var host: String = JHipsterDefaults.Logging.Logstash.host,

            var port: Int = JHipsterDefaults.Logging.Logstash.port,

            var queueSize: Int = JHipsterDefaults.Logging.Logstash.queueSize
        )
    }

    data class Social(
        var redirectAfterSignIn: String = JHipsterDefaults.Social.redirectAfterSignIn
    )

    data class Gateway(
        val rateLimiting: RateLimiting = RateLimiting(),

        var authorizedMicroservicesEndpoints: Map<String?, List<String?>?> =
            JHipsterDefaults.Gateway.authorizedMicroservicesEndpoints
    ) {

        data class RateLimiting(
            var isEnabled: Boolean = JHipsterDefaults.Gateway.RateLimiting.enabled,

            var limit: Long = JHipsterDefaults.Gateway.RateLimiting.limit,

            var durationInSeconds: Int = JHipsterDefaults.Gateway.RateLimiting.durationInSeconds
        )
    }

    data class Registry(
        var password: String? = JHipsterDefaults.Registry.password
    )

    data class ClientApp(
        var name: String = JHipsterDefaults.ClientApp.name
    )

    data class AuditEvents(
        var retentionPeriod: Int = JHipsterDefaults.AuditEvents.retentionPeriod
    )
}
