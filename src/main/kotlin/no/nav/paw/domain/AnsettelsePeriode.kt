package no.nav.paw.domain

import no.nav.paw.aareg.Arbeidsforhold
import java.time.LocalDate

data class Perioder(
    val perioder: List<Periode>
) {
    fun fraArbeidsforhold(arbeidsforhold: List<Arbeidsforhold>) {
        arbeidsforhold
            .map { it.ansettelsesperiode.periode }
            .map { Periode(it.fom, it.tom ?: LocalDate.now()) }
            .sortedByDescending { it.fom }
    }
}

data class Periode(
    val fom: LocalDate,
    val tom: LocalDate
)
