package no.nav.paw.domain

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class SlaaSammenPerioder {
    @Test
    fun `slå sammen perioder med opphold med 3 dager eller mindre`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 3)),
            EndeligPeriode(LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 7)),
            EndeligPeriode(LocalDate.of(2022, 1, 8), LocalDate.of(2022, 1, 10)),
            EndeligPeriode(LocalDate.of(2022, 1, 12), LocalDate.of(2022, 1, 13))
        )

        val forventetResultat = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 13))
        )

        assertEquals(forventetResultat, perioder.slaaSammenPerioder())
    }

    @Test
    fun `slår sammen overlappende perioder`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10)),
            EndeligPeriode(LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 15)),
            EndeligPeriode(LocalDate.of(2022, 1, 20), LocalDate.of(2022, 1, 30))
        )
        val forventetResultat = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 15)),
            EndeligPeriode(LocalDate.of(2022, 1, 20), LocalDate.of(2022, 1, 30))
        )

        assertEquals(forventetResultat, perioder.slaaSammenPerioder())
    }

    @Test
    fun `slaaSammenPerioder slår ikke sammen perioder som ikke overlapper`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10)),
            EndeligPeriode(LocalDate.of(2022, 1, 15), LocalDate.of(2022, 1, 20)),
            EndeligPeriode(LocalDate.of(2022, 1, 25), LocalDate.of(2022, 1, 30))
        )
        val forventetResultat = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10)),
            EndeligPeriode(LocalDate.of(2022, 1, 15), LocalDate.of(2022, 1, 20)),
            EndeligPeriode(LocalDate.of(2022, 1, 25), LocalDate.of(2022, 1, 30))
        )

        assertEquals(forventetResultat, perioder.slaaSammenPerioder())
    }

    @Test
    fun `slaaSammenPerioder slår sammen perioder med opphold på mindre enn antallDagerOpphold`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10)),
            EndeligPeriode(LocalDate.of(2022, 1, 12), LocalDate.of(2022, 1, 15)),
            EndeligPeriode(LocalDate.of(2022, 1, 16), LocalDate.of(2022, 1, 20))
        )
        val forventetResultat = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 20))
        )

        assertEquals(forventetResultat, perioder.slaaSammenPerioder())
    }
}
