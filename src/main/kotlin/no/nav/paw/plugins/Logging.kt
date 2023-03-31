package no.nav.paw.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.request.path
import java.util.*

fun Application.configureLogging() {
    install(CallId) {
        retrieveFromHeader("Nav-Call-Id")
        generate { UUID.randomUUID().toString() }
        verify { it.isNotEmpty() }
    }
    install(CallLogging) {
        callIdMdc("x_correlationId")
        disableDefaultColors()
        filter { !it.request.path().startsWith("/internal") }
    }
}
