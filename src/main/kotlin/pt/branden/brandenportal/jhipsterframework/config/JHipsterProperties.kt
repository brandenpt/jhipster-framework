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
class JHipsterProperties {

    val async = Async()

    val http = Http()

    val cache = Cache()

    val mail = Mail()

    val security = Security()

    val swagger = Swagger()

    val metrics = Metrics()

    val logging = Logging()

    val cors = CorsConfiguration()

    val social = Social()

    val gateway = Gateway()

    val registry = Registry()

    val clientApp = ClientApp()

    val auditEvents = AuditEvents()

    class Async {
        var corePoolSize =
            JHipsterDefaults.Async.corePoolSize
        var maxPoolSize =
            JHipsterDefaults.Async.maxPoolSize
        var queueCapacity =
            JHipsterDefaults.Async.queueCapacity
    }

    class Http {
        val cache = Cache()

        class Cache {
            var timeToLiveInDays =
                JHipsterDefaults.Http.Cache.timeToLiveInDays
        }
    }

    class Cache {
        val hazelcast = Hazelcast()
        val caffeine = Caffeine()
        val ehcache = Ehcache()
        val infinispan = Infinispan()
        val memcached = Memcached()
        val redis = Redis()

        class Hazelcast {
            var timeToLiveSeconds =
                JHipsterDefaults.Cache.Hazelcast.timeToLiveSeconds
            var backupCount =
                JHipsterDefaults.Cache.Hazelcast.backupCount
            val managementCenter = ManagementCenter()

            class ManagementCenter {
                var isEnabled =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.enabled
                var updateInterval =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.updateInterval
                var url =
                    JHipsterDefaults.Cache.Hazelcast.ManagementCenter.url
            }
        }

        class Caffeine {
            var timeToLiveSeconds =
                JHipsterDefaults.Cache.Caffeine.timeToLiveSeconds
            var maxEntries =
                JHipsterDefaults.Cache.Caffeine.maxEntries
        }

        class Ehcache {
            var timeToLiveSeconds =
                JHipsterDefaults.Cache.Ehcache.timeToLiveSeconds
            var maxEntries =
                JHipsterDefaults.Cache.Ehcache.maxEntries
        }

        class Infinispan {
            var configFile =
                JHipsterDefaults.Cache.Infinispan.configFile
            var isStatsEnabled =
                JHipsterDefaults.Cache.Infinispan.statsEnabled
            val local = Local()
            val distributed = Distributed()
            val replicated = Replicated()

            class Local {
                var timeToLiveSeconds =
                    JHipsterDefaults.Cache.Infinispan.Local.timeToLiveSeconds
                var maxEntries =
                    JHipsterDefaults.Cache.Infinispan.Local.maxEntries
            }

            class Distributed {
                var timeToLiveSeconds =
                    JHipsterDefaults.Cache.Infinispan.Distributed.timeToLiveSeconds
                var maxEntries =
                    JHipsterDefaults.Cache.Infinispan.Distributed.maxEntries
                var instanceCount =
                    JHipsterDefaults.Cache.Infinispan.Distributed.instanceCount
            }

            class Replicated {
                var timeToLiveSeconds =
                    JHipsterDefaults.Cache.Infinispan.Replicated.timeToLiveSeconds
                var maxEntries =
                    JHipsterDefaults.Cache.Infinispan.Replicated.maxEntries
            }
        }

        class Memcached {
            var isEnabled =
                JHipsterDefaults.Cache.Memcached.enabled
            /**
             * Comma or whitespace separated list of servers' addresses.
             */
            var servers =
                JHipsterDefaults.Cache.Memcached.servers
            var expiration =
                JHipsterDefaults.Cache.Memcached.expiration
            var isUseBinaryProtocol =
                JHipsterDefaults.Cache.Memcached.useBinaryProtocol
        }

        class Redis {
            var server =
                JHipsterDefaults.Cache.Redis.server
            var expiration =
                JHipsterDefaults.Cache.Redis.expiration
            var cluster =
                JHipsterDefaults.Cache.Redis.cluster
        }
    }

    class Mail {
        var isEnabled =
            JHipsterDefaults.Mail.enabled
        var from = JHipsterDefaults.Mail.from
        var baseUrl =
            JHipsterDefaults.Mail.baseUrl
    }

    class Security {
        val clientAuthorization = ClientAuthorization()
        val authentication = Authentication()
        val rememberMe = RememberMe()
        val oauth2 = OAuth2()

        class ClientAuthorization {
            var accessTokenUri =
                JHipsterDefaults.Security.ClientAuthorization.accessTokenUri
            var tokenServiceId =
                JHipsterDefaults.Security.ClientAuthorization.tokenServiceId
            var clientId =
                JHipsterDefaults.Security.ClientAuthorization.clientId
            var clientSecret =
                JHipsterDefaults.Security.ClientAuthorization.clientSecret
        }

        class Authentication {
            val jwt = Jwt()

            class Jwt {
                var secret =
                    JHipsterDefaults.Security.Authentication.Jwt.secret
                var base64Secret =
                    JHipsterDefaults.Security.Authentication.Jwt.base64Secret
                var tokenValidityInSeconds =
                    JHipsterDefaults.Security.Authentication.Jwt
                        .tokenValidityInSeconds
                var tokenValidityInSecondsForRememberMe =
                    JHipsterDefaults.Security.Authentication.Jwt
                        .tokenValidityInSecondsForRememberMe
            }
        }

        class RememberMe {
            var key =
                JHipsterDefaults.Security.RememberMe.key
        }

        class OAuth2 {
            private var _audience = mutableListOf<String>()

            var audience: List<String>
                get() = _audience
                set(value) = run { _audience.addAll(value) }
        }
    }

    class Swagger {
        var title =
            JHipsterDefaults.Swagger.title
        var description =
            JHipsterDefaults.Swagger.description
        var version =
            JHipsterDefaults.Swagger.version
        var termsOfServiceUrl =
            JHipsterDefaults.Swagger.termsOfServiceUrl
        var contactName =
            JHipsterDefaults.Swagger.contactName
        var contactUrl =
            JHipsterDefaults.Swagger.contactUrl
        var contactEmail =
            JHipsterDefaults.Swagger.contactEmail
        var license =
            JHipsterDefaults.Swagger.license
        var licenseUrl =
            JHipsterDefaults.Swagger.licenseUrl
        var defaultIncludePattern =
            JHipsterDefaults.Swagger.defaultIncludePattern
        var host =
            JHipsterDefaults.Swagger.host
        var protocols =
            JHipsterDefaults.Swagger.protocols
        var isUseDefaultResponseMessages =
            JHipsterDefaults.Swagger.useDefaultResponseMessages
    }

    class Metrics {
        val logs = Logs()

        class Logs {
            var isEnabled =
                JHipsterDefaults.Metrics.Logs.enabled
            var reportFrequency =
                JHipsterDefaults.Metrics.Logs.reportFrequency
        }
    }

    class Logging {
        var isUseJsonFormat =
            JHipsterDefaults.Logging.useJsonFormat
        val logstash = Logstash()

        class Logstash {
            var isEnabled =
                JHipsterDefaults.Logging.Logstash.enabled
            var host =
                JHipsterDefaults.Logging.Logstash.host
            var port =
                JHipsterDefaults.Logging.Logstash.port
            var queueSize =
                JHipsterDefaults.Logging.Logstash.queueSize
        }
    }

    class Social {
        var redirectAfterSignIn =
            JHipsterDefaults.Social.redirectAfterSignIn
    }

    class Gateway {
        val rateLimiting = RateLimiting()

        var authorizedMicroservicesEndpoints =
            JHipsterDefaults.Gateway
                .authorizedMicroservicesEndpoints

        class RateLimiting {
            var isEnabled =
                JHipsterDefaults.Gateway.RateLimiting.enabled
            var limit =
                JHipsterDefaults.Gateway.RateLimiting.limit
            var durationInSeconds =
                JHipsterDefaults.Gateway.RateLimiting.durationInSeconds
        }
    }

    class Registry {
        var password =
            JHipsterDefaults.Registry.password
    }

    class ClientApp {
        var name =
            JHipsterDefaults.ClientApp.name
    }

    class AuditEvents {
        var retentionPeriod =
            JHipsterDefaults.AuditEvents.retentionPeriod
    }
}
