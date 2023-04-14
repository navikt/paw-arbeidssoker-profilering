package no.nav.paw.domain

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HarJobbetSammenhengedeSeksAvTolvSisteManderTest {

    val dagensDato = LocalDate.of(2023, 12, 1)

    @Test
    fun `skal returnere true når man har jobbet sammenhengende seks av tolv siste måneder`() {
        val perioder = listOf(
            EndeligPeriode(
                LocalDate.of(2023, 7, 1),
                LocalDate.of(2023, 12, 1)
            )
        )
        assertTrue(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(dagensDato))
    }

    @Test
    fun `skal returne false når periode listen er tom`() {
        val tomListe = emptyList<EndeligPeriode>()
        assertFalse(tomListe.harJobbetSammenhengendeSeksAvTolvSisteMnd(dagensDato))
    }

    @Test
    fun `skal returne false når perioder ikke overlapper med de siste 12 måneder`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 6, 30)),
            EndeligPeriode(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 12, 31))
        )
        assertFalse(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(LocalDate.of(2023, 4, 4)))
    }

    @Test
    fun `skal returne false når perioder overlapper med de siste 12 måneder, men ikke sammenhengende i minst 6 måneder`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 6, 30)),
            EndeligPeriode(LocalDate.of(2021, 6, 1), LocalDate.of(2021, 12, 31))
        )
        assertFalse(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(LocalDate.of(2023, 4, 4)))
    }

    @Test
    fun `skal returne true når perioder overlapper sammenhengende for minst 6 måneder innen de siste 12 måneder`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 6, 30)),
            EndeligPeriode(LocalDate.of(2021, 12, 1), LocalDate.of(2022, 5, 31))
        )
        assertTrue(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(LocalDate.of(2022, 8, 1)))
    }

    @Test
    fun `skal returnere true når personen har jobbet sammenhengende i 6 av de siste 12 månedene`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 28)),
            EndeligPeriode(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)),
            EndeligPeriode(LocalDate.of(2022, 4, 1), LocalDate.of(2022, 6, 30)),
            EndeligPeriode(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 31)),
            EndeligPeriode(LocalDate.of(2022, 8, 1), LocalDate.of(2022, 8, 31)),
            EndeligPeriode(LocalDate.of(2022, 9, 1), LocalDate.of(2022, 10, 31))
        )

        assertTrue(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(LocalDate.of(2023, 4, 1)))
    }

    @Test
    fun `skal returnere false når personen ikke har jobbet sammenhengende i 6 av de siste 12 månedene`() {
        val perioder = listOf(
            EndeligPeriode(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 28)),
            EndeligPeriode(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)),
            EndeligPeriode(LocalDate.of(2022, 4, 1), LocalDate.of(2022, 6, 30)),
            EndeligPeriode(LocalDate.of(2022, 7, 1), LocalDate.of(2022, 7, 31)),
            EndeligPeriode(LocalDate.of(2022, 8, 1), LocalDate.of(2022, 8, 31))
        )

        assertFalse(perioder.harJobbetSammenhengendeSeksAvTolvSisteMnd(LocalDate.of(2023, 4, 1)))
    }
}
