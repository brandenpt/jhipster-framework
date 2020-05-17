/*
 * Copyright (c) 2019 Branden(r). All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r), Portuguese registered company.
 * Last modified 13/11/19, 10:25.
 */

package pt.branden.brandenportal.jhipsterframework.config.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.boolex.OnMarkerEvaluator
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.filter.EvaluatorFilter
import ch.qos.logback.core.spi.ContextAwareBase
import ch.qos.logback.core.spi.FilterReply
import java.net.InetSocketAddress
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.composite.ContextJsonProvider
import net.logstash.logback.composite.GlobalCustomFieldsJsonProvider
import net.logstash.logback.composite.loggingevent.*
import net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder
import net.logstash.logback.encoder.LogstashEncoder
import net.logstash.logback.stacktrace.ShortenedThrowableConverter
import org.slf4j.LoggerFactory
import pt.branden.brandenportal.jhipsterframework.config.JHipsterProperties

/**
 * Utility methods to add appenders to a [LoggerContext].
 */
@Suppress("TooManyFunctions")
object LoggingUtils {
    private val log = LoggerFactory.getLogger(javaClass)

    private const val SHORTENED_LOGGER_NAME_LENGTH = 20
    private const val CONSOLE_APPENDER_NAME = "CONSOLE"
    private const val LOGSTASH_APPENDER_NAME = "LOGSTASH"
    private const val ASYNC_LOGSTASH_APPENDER_NAME = "ASYNC_LOGSTASH"

    /**
     * <p>addJsonConsoleAppender.</p>
     *
     * @param context a [LoggerContext] object.
     * @param customFields a [String] object.
     */
    fun addJsonConsoleAppender(context: LoggerContext, customFields: String?) {
        log.info("Initializing Console loggingProperties")

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        val consoleAppender = ConsoleAppender<ILoggingEvent?>().apply {
            this.context = context
            encoder = compositeJsonEncoder(context, customFields)
            name = CONSOLE_APPENDER_NAME
            start()
        }

        context.getLogger(Logger.ROOT_LOGGER_NAME).detachAppender(CONSOLE_APPENDER_NAME)
        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(consoleAppender)
    }

    /**
     * <p>addLogstashTcpSocketAppender.</p>
     *
     * @param context a {@link ch.qos.logback.classic.LoggerContext} object.
     * @param customFields a {@link java.lang.String} object.
     * @param logstashProperties a {@link io.github.jhipster.config.JHipsterProperties.Logging.Logstash} object.
     */
    fun addLogstashTcpSocketAppender(
        context: LoggerContext,
        customFields: String?,
        logstashProperties: JHipsterProperties.Logging.Logstash
    ) {
        log.info("Initializing Logstash loggingProperties")

        // More documentation is available at: https://github.com/logstash/logstash-logback-encoder
        val logstashAppender = LogstashTcpSocketAppender().apply {
            addDestinations(InetSocketAddress(logstashProperties.host, logstashProperties.port))
            this.context = context
            encoder = logstashEncoder(customFields)
            name = ASYNC_LOGSTASH_APPENDER_NAME
            queueSize = logstashProperties.queueSize
            start()
        }

        context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(logstashAppender)
    }

    fun addContextListener(context: LoggerContext, customFields: String, properties: JHipsterProperties.Logging) {
        val loggerContextListener = LogbackLoggerContextListener(properties, customFields).apply {
            this.context = (context)
        }
        context.addListener(loggerContextListener)
    }

    /**
     * Configure a log filter to remove "metrics" logs from all appenders except the "LOGSTASH" appender
     *
     * @param context the logger context
     * @param useJsonFormat whether to use JSON format
     */
    fun setMetricsMarkerLogbackFilter(context: LoggerContext, useJsonFormat: Boolean) {
        log.info("Filtering metrics logs from all appenders except the {} appender", LOGSTASH_APPENDER_NAME)

        val onMarkerMetricsEvaluator = OnMarkerEvaluator().apply {
            this.context = context
            addMarker("metrics")
            start()
        }

        val metricsFilter = EvaluatorFilter<ILoggingEvent>().apply {
            this.context = context
            evaluator = onMarkerMetricsEvaluator
            onMatch = FilterReply.DENY
            start()
        }

        context.loggerList.forEach {
            it.iteratorForAppenders().forEachRemaining { appender ->
                if (appender.name != ASYNC_LOGSTASH_APPENDER_NAME &&
                    !(appender.name == CONSOLE_APPENDER_NAME && useJsonFormat)
                ) {
                    log.debug("Filter metrics logs from the {} appender", appender.name)
                    appender.context = context
                    appender.addFilter(metricsFilter)
                    appender.start()
                }
            }
        }
    }

    private fun compositeJsonEncoder(
        context: LoggerContext?,
        customFields: String?
    ): LoggingEventCompositeJsonEncoder {
        return LoggingEventCompositeJsonEncoder().apply {
            this.context = context
            providers = jsonProviders(context, customFields)
            start()
        }
    }

    private fun jsonProviders(
        context: LoggerContext?,
        customFields: String?
    ): LoggingEventJsonProviders {
        return LoggingEventJsonProviders().apply {
            addArguments(ArgumentsJsonProvider())
            addContext(ContextJsonProvider())
            addGlobalCustomFields(customFieldsJsonProvider(customFields))
            addLogLevel(LogLevelJsonProvider())
            addLoggerName(loggerNameJsonProvider())
            addMdc(MdcJsonProvider())
            addMessage(MessageJsonProvider())
            addPattern(LoggingEventPatternJsonProvider())
            addStackTrace(stackTraceJsonProvider())
            addThreadName(ThreadNameJsonProvider())
            addTimestamp(timestampJsonProvider())
            setContext(context)
        }
    }

    private fun customFieldsJsonProvider(customFields: String?): GlobalCustomFieldsJsonProvider<ILoggingEvent> {
        return GlobalCustomFieldsJsonProvider<ILoggingEvent>().apply { this.customFields = customFields }
    }

    private fun loggerNameJsonProvider(): LoggerNameJsonProvider {
        return LoggerNameJsonProvider().apply { shortenedLoggerNameLength = SHORTENED_LOGGER_NAME_LENGTH }
    }

    private fun stackTraceJsonProvider(): StackTraceJsonProvider {
        return StackTraceJsonProvider().apply { throwableConverter = throwableConverter() }
    }

    private fun throwableConverter(): ShortenedThrowableConverter {
        return ShortenedThrowableConverter().apply { isRootCauseFirst = true }
    }

    private fun timestampJsonProvider(): LoggingEventFormattedTimestampJsonProvider {
        return LoggingEventFormattedTimestampJsonProvider().apply {
            timeZone = "UTC"
            fieldName = "timestamp"
        }
    }

    private fun logstashEncoder(customFields: String?): LogstashEncoder {
        return LogstashEncoder().apply {
            throwableConverter = throwableConverter()
            this.customFields = customFields
        }
    }

    /**
     * Logback configuration is achieved by configuration file and API.
     * When configuration file change is detected, the configuration is reset.
     * This listener ensures that the programmatic configuration is also re-applied after reset.
     */
    class LogbackLoggerContextListener internal constructor(
        private val loggingProperties: JHipsterProperties.Logging,
        private val customFields: String
    ) : ContextAwareBase(), LoggerContextListener {
        override fun isResetResistant() = true

        override fun onStart(context: LoggerContext) {
            if (loggingProperties.isUseJsonFormat) {
                addJsonConsoleAppender(context, customFields)
            }
            if (loggingProperties.logstash.isEnabled) {
                addLogstashTcpSocketAppender(context, customFields, loggingProperties.logstash)
            }
        }

        override fun onReset(context: LoggerContext) {
            onStart(context)
        }

        override fun onStop(context: LoggerContext?) {
            // Nothing to do.
        }

        override fun onLevelChange(logger: Logger?, level: Level?) {
            // Nothing to do.
        }
    }
}
