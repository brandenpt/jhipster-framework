/*
 * Copyright 2016-2019 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
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

class JHipsterDefaults {
    interface Async {
        companion object {
            const val corePoolSize = 2
            const val maxPoolSize = 50
            const val queueCapacity = 10000
        }
    }

    interface Http {
        interface Cache {
            companion object {
                const val timeToLiveInDays = 1461 // 4 years (including leap day)
            }
        }
    }

    interface Cache {
        interface Hazelcast {
            interface ManagementCenter {
                companion object {
                    const val enabled = false
                    const val updateInterval = 3
                    const val url = ""
                }
            }

            companion object {
                const val timeToLiveSeconds = 3600 // 1 hour

                const val backupCount = 1
            }
        }

        interface Caffeine {
            companion object {
                const val timeToLiveSeconds = 3600 // 1 hour

                const val maxEntries: Long = 100
            }
        }

        interface Ehcache {
            companion object {
                const val timeToLiveSeconds = 3600 // 1 hour

                const val maxEntries: Long = 100
            }
        }

        interface Infinispan {
            interface Local {
                companion object {
                    const val timeToLiveSeconds: Long = 60 // 1 minute

                    const val maxEntries: Long = 100
                }
            }

            interface Distributed {
                companion object {
                    const val timeToLiveSeconds: Long = 60 // 1 minute

                    const val maxEntries: Long = 100
                    const val instanceCount = 1
                }
            }

            interface Replicated {
                companion object {
                    const val timeToLiveSeconds: Long = 60 // 1 minute

                    const val maxEntries: Long = 100
                }
            }

            companion object {
                const val configFile = "default-configs/default-jgroups-tcp.xml"
                const val statsEnabled = false
            }
        }

        interface Memcached {
            companion object {
                const val enabled = false
                const val servers = "localhost:11211"
                const val expiration = 300 // 5 minutes

                const val useBinaryProtocol = true
            }
        }

        interface Redis {
            companion object {
                const val server = "redis://localhost:6379"
                const val expiration = 300 // 5 minutes
                const val cluster = false
            }
        }
    }

    interface Mail {
        companion object {
            const val enabled = false
            const val from = ""
            const val baseUrl = ""
        }
    }

    interface Security {
        interface ClientAuthorization {
            companion object {
                val accessTokenUri: String? = null
                val tokenServiceId: String? = null
                val clientId: String? = null
                val clientSecret: String? = null
            }
        }

        interface Authentication {
            interface Jwt {
                companion object {
                    val secret: String? = null
                    val base64Secret: String? = null
                    const val tokenValidityInSeconds: Long = 1800 // 30 minutes

                    const val tokenValidityInSecondsForRememberMe: Long = 2592000 // 30 days
                }
            }
        }

        interface RememberMe {
            companion object {
                val key: String? = null
            }
        }
    }

    interface Swagger {
        companion object {
            const val title = "Application API"
            const val description = "API documentation"
            const val version = "0.0.1"
            val termsOfServiceUrl: String? = null
            val contactName: String? = null
            val contactUrl: String? = null
            val contactEmail: String? = null
            val license: String? = null
            val licenseUrl: String? = null
            const val defaultIncludePattern = "/api/.*"
            val host: String? = null
            val protocols = arrayOf<String>()
            const val useDefaultResponseMessages = true
        }
    }

    interface Metrics {
        interface Jmx {
            companion object {
                const val enabled = false
            }
        }

        interface Logs {
            companion object {
                const val enabled = false
                const val reportFrequency: Long = 60
            }
        }

        interface Prometheus {
            companion object {
                const val enabled = false
                const val endpoint = "/prometheusMetrics"
            }
        }
    }

    interface Logging {
        interface Logstash {
            companion object {
                const val enabled = false
                const val host = "localhost"
                const val port = 5000
                const val queueSize = 512
            }
        }

        companion object {
            const val useJsonFormat = false
        }
    }

    interface Social {
        companion object {
            const val redirectAfterSignIn = "/#/home"
        }
    }

    interface Gateway {
        interface RateLimiting {
            companion object {
                const val enabled = false
                const val limit = 100000L
                const val durationInSeconds = 3600
            }
        }

        companion object {
            val authorizedMicroservicesEndpoints: Map<String?, List<String?>?> =
                LinkedHashMap()
        }
    }

    interface Ribbon {
        companion object {
            val displayOnActiveProfiles: Array<String?>? = null
        }
    }

    interface Registry {
        companion object {
            val password: String? = null
        }
    }

    interface ClientApp {
        companion object {
            const val name = "jhipsterApp"
        }
    }

    interface AuditEvents {
        companion object {
            const val retentionPeriod = 30
        }
    }
}
