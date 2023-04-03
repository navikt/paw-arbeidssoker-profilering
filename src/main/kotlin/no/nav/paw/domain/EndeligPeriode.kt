package no.nav.paw.domain

import no.nav.paw.aareg.Arbeidsforhold
import java.time.LocalDate

data class EndeligPeriode(
    val fom: LocalDate,
    val tom: LocalDate
)

fun List<Arbeidsforhold>.tilEndeligePerioder(): List<EndeligPeriode> = this
    .map { it.ansettelsesperiode.periode }
    .map { EndeligPeriode(it.fom, it.tom ?: LocalDate.now()) }
    .sortedByDescending { it.fom }

fun List<EndeligPeriode>.harJobbetSammenhengendeSeksAvTolvSisteManeder(dagensDato: LocalDate = LocalDate.now()): Boolean {
    val siste12Maaneder = (0..11).map { dagensDato.minusMonths(it.toLong()) }.toSet()
    val jobbMaaneder = this.flatMap { it.fom.datesUntil(it.tom.plusDays(1)).toList() }.filter { it in siste12Maaneder }
    val antallSammenhengendeMaaneder = jobbMaaneder.fold(0) { antall, dato ->
        if (antall > 0 && dato.minusMonths(1) !in jobbMaaneder) 1 else antall + 1
    }
    return antallSammenhengendeMaaneder >= 6
}
