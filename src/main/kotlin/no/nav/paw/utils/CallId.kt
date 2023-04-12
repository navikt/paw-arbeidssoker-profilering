package no.nav.paw.utils

import org.slf4j.MDC
import java.util.*

object CallId {
    val callId: String get() = MDC.get("callId") ?: UUID.randomUUID().toString()

    fun leggTilCallId() = MDC.put("callId", UUID.randomUUID().toString())
}
