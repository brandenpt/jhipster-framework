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

package pt.branden.brandenportal.jhipsterframework.config.locale

import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.context.i18n.LocaleContext
import org.springframework.context.i18n.TimeZoneAwareLocaleContext
import org.springframework.util.StringUtils
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.util.WebUtils

class AngularCookieLocaleResolver : CookieLocaleResolver() {

    override fun resolveLocale(request: HttpServletRequest): Locale {
        parseLocaleCookieIfNecessary(request)
        return request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) as Locale
    }

    override fun resolveLocaleContext(request: HttpServletRequest): LocaleContext {
        parseLocaleCookieIfNecessary(request)
        return object : TimeZoneAwareLocaleContext {
            override fun getLocale(): Locale {
                return request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) as Locale
            }

            override fun getTimeZone(): TimeZone {
                return request.getAttribute(TIME_ZONE_REQUEST_ATTRIBUTE_NAME) as TimeZone
            }
        }
    }

    override fun addCookie(response: HttpServletResponse, cookieValue: String) {
        // Mandatory cookie modification for AngularJS to support the locale switching on the server side.
        super.addCookie(response, quote(cookieValue))
    }

    private fun parseLocaleCookieIfNecessary(request: HttpServletRequest) {
        request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME)?.let { return }
        // Retrieve and parse cookie value.

        var locale: Locale? = null
        var timeZone: TimeZone? = null
        WebUtils.getCookie(request, cookieName!!)?.let { cookie ->
            var value: String = cookie.value

            // Remove the double quote
            value = StringUtils.replace(value, QUOTE, "")

            var localePart = value
            var timeZonePart: String? = null
            val spaceIndex = localePart.indexOf(' ')
            if (spaceIndex != -1) {
                localePart = value.substring(0, spaceIndex)
                timeZonePart = value.substring(spaceIndex + 1)
            }

            locale = if ("-" != localePart) StringUtils.parseLocaleString(localePart.replace('-', '_'))
                    else null

            timeZonePart?.let { timeZone = StringUtils.parseTimeZoneString(it) }

            if (logger.isTraceEnabled) {
                logger.trace(
                    "Parsed cookie value [" + cookie.value.toString() + "] into locale '" + locale.toString() +
                            "'" + timeZone?.let { " and time zone '" + it.id + "'" }
                )
            }
        }
        request.setAttribute(
            LOCALE_REQUEST_ATTRIBUTE_NAME,
            locale ?: determineDefaultLocale(request)
        )
        request.setAttribute(
            TIME_ZONE_REQUEST_ATTRIBUTE_NAME,
            timeZone ?: determineDefaultTimeZone(request)
        )
    }

    private fun quote(string: String): String {
        return QUOTE + string + QUOTE
    }

    companion object {
        /** Constant <code>QUOTE="%22"</code> */
        const val QUOTE = "%22"
    }
}
