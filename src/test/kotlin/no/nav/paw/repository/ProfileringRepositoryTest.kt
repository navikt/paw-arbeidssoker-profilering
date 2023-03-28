package no.nav.paw.repository

import no.nav.paw.ProfileringTestData
import no.nav.paw.utils.TestDatabase
import javax.sql.DataSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileringRepositoryTest {
    private lateinit var dataSource: DataSource
    private lateinit var repository: ProfileringRepository

    @BeforeTest
    fun before() {
        dataSource = TestDatabase.setup()
        repository = ProfileringRepository(dataSource)
    }

    @Test
    fun `opprett skal sette inn en nytt profileringsoppføring`() {
        val resultat = repository.opprett(
            ProfileringTestData.foedselsnummer,
            ProfileringTestData.profilering,
            ProfileringTestData.besvarelse
        )

        assertEquals(1, resultat)
    }

    @Test
    fun `hentSiste skal returnere den siste profilingsoppføringen`() {
        repository.opprett(
            ProfileringTestData.foedselsnummer,
            ProfileringTestData.profilering,
            ProfileringTestData.besvarelse
        )

        val resultat = repository.hentSiste(ProfileringTestData.foedselsnummer)

        println(resultat)
        assertEquals(ProfileringTestData.profilering.innsatsgruppe, resultat?.innsatsgruppe)
    }
}