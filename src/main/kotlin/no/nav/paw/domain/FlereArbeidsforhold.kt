package no.nav.paw.domain

import java.time.LocalDate
import java.util.*
import kotlin.Comparator

data class FlereArbeidsforhold(val flereArbeidsforhold: List<Arbeidsforhold>) {
    /**
     * En bruker som har jobbet sammenhengende i seks av de siste tolv månedene oppfyller betingelsen om arbeidserfaring
     */
    fun harJobbetSammenhengendeSeksAvTolvSisteManeder(dagensDato: LocalDate): Boolean =
        harJobbetSammenhengendeSisteManeder(dagensDato, 6, 12)

    fun harJobbetSammenhengendeSisteManeder(
        dagensDato: LocalDate,
        minAntallMndSammenhengendeJobb: Int,
        antallMnd: Int
    ): Boolean {
        var antallSammenhengendeMandeder = 0
        var mndFraDagensMnd = 0
        var innevaerendeMnd = dagensDato.withDayOfMonth(1)

        while (antallSammenhengendeMandeder < minAntallMndSammenhengendeJobb && mndFraDagensMnd < antallMnd) {
            if (harArbeidsforholdPaaDato(innevaerendeMnd)) {
                antallSammenhengendeMandeder++
            } else {
                antallSammenhengendeMandeder = 0
            }
            innevaerendeMnd = innevaerendeMnd.minusMonths(1)
            mndFraDagensMnd += 1
        }
        return antallSammenhengendeMandeder >= minAntallMndSammenhengendeJobb
    }

    fun harArbeidsforholdPaaDato(innevaerendeMnd: LocalDate): Boolean =
        flereArbeidsforhold.any {
            it.erDatoInnenforPeriode(innevaerendeMnd)
        }

    companion object {

        private fun sorterArbeidsforholdEtterTilDato(): Comparator<Arbeidsforhold> {
            return Comparator.comparing(Arbeidsforhold::tom, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed()
        }
    }
}

data class Arbeidsforhold(
    val arbeidsgiverOrgnummer: String?,
    val styrk: String = "utenstyrkkode",
    val fom: LocalDate?,
    val tom: LocalDate?,
    private val navArbeidsforholdId: String?
) {
        fun erDatoInnenforPeriode(innevaerendeMnd: LocalDate): Boolean {
            return innevaerendeMnd.isAfter(fom!!.minusDays(1)) &&
                    (Objects.isNull(tom) || innevaerendeMnd.isBefore(tom!!.plusDays(1)))
        }
}