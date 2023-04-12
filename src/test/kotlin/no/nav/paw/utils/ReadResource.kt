package no.nav.paw.utils

internal fun String.readResource(): String =
    ClassLoader.getSystemResource(this).readText()
