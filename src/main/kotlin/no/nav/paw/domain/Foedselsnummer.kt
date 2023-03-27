package no.nav.paw.domain

import com.nimbusds.jwt.util.DateUtils
import no.bekk.bekkopen.person.FodselsnummerValidator
import no.nav.paw.domain.FnrUtils.alderForFnr
import java.time.LocalDate
import java.time.Period

@JvmInline
value class Foedselsnummer(val verdi: String) {
    val alder: Int get() = alderForFnr(verdi, LocalDate.now())
    override fun toString(): String {
        return "*".repeat(11)
    }
}

internal object FnrUtils {
    fun alderForFnr(fnr: String, dagensDato: LocalDate): Int {
        return antallAarSidenDato(utledFodselsdatoForFnr(fnr), dagensDato)
    }

    fun utledFodselsdatoForFnr(fnr: String): LocalDate {
        val fodselsnummer = FodselsnummerValidator.getFodselsnummer(fnr)
        return LocalDate.of(
            fodselsnummer.birthYear.toInt(),
            fodselsnummer.month.toInt(),
            fodselsnummer.dayInMonth.toInt()
        )
    }

    fun antallAarSidenDato(dato: LocalDate?, dagensDato: LocalDate): Int {
        return Period.between(dato, dagensDato).years
    }
}
