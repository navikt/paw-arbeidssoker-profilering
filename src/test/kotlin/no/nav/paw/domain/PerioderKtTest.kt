package no.nav.paw.domain

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class PerioderKtTest {

    val dagensDato = LocalDate.of(2023, 12, 1)

    @Test
    fun harJobbetSammenhengendeSeksAvTolvSisteManeder() {
        val arbeidsforhold = listOf(
            EndeligPeriode(
                LocalDate.of(2023, 7, 1),
                LocalDate.of(2023, 12, 1)
            )
        )
        assertEquals(true, arbeidsforhold.harJobbetSammenhengendeSeksAvTolvSisteManeder(dagensDato))
    }
}
