package no.nav.paw.utils

import org.slf4j.LoggerFactory

inline val <reified T : Any> T.logger get() = LoggerFactory.getLogger(T::class.java.name)
inline val secureLogger get() = LoggerFactory.getLogger("Tjenestekall")
