package no.nav.paw.domain

import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.paw.config.NaisEnv
import java.time.LocalDate
import java.time.Period

data class Foedselsnummer(val foedselsnummer: String) {
    val alder: Int get() = alderForFnr(foedselsnummer, LocalDate.now())
    override fun toString(): String = "*".repeat(11)
}

private fun alderForFnr(fnr: String, dagensDato: LocalDate): Int =
    antallAarSidenDato(utledFodselsdatoForFnr(fnr), dagensDato)

private fun utledFodselsdatoForFnr(fnr: String): LocalDate {
    if (!NaisEnv.current().isProdGCP()) {
        FodselsnummerValidator.ALLOW_SYNTHETIC_NUMBERS = true
    }
    val fodselsnummer = FodselsnummerValidator.getFodselsnummer(fnr)
    return LocalDate.of(
        fodselsnummer.birthYear.toInt(),
        fodselsnummer.month.toInt(),
        fodselsnummer.dayInMonth.toInt()
    )
}

fun antallAarSidenDato(dato: LocalDate?, dagensDato: LocalDate): Int =
    Period.between(dato, dagensDato).years
