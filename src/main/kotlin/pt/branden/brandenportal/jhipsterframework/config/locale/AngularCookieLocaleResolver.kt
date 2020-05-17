/*
 * Copyright (c) 2019 Branden(r). All rights reserved.
 * Developed by Fernando Rodrigues for BrandenPortal.
 * Project of Branden(r), Portuguese registered company.
 * Last modified 13/11/19, 09:40.
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
