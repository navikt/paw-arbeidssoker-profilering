package no.nav.paw.repository

import no.nav.paw.ProfileringTestData
import no.nav.paw.config.Config
import no.nav.paw.createConfig
import no.nav.paw.utils.TestDatabase
import no.nav.paw.utils.TestDatabase.setupDataSource
import no.nav.paw.utils.TestKafka
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProfileringRepositoryTest {
    companion object {
        private lateinit var postgreSQLContainer: PostgreSQLContainer<*>
        private lateinit var dataSource: DataSource
        private lateinit var repository: ProfileringRepository

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            postgreSQLContainer = TestDatabase.setup()

            dataSource = setupDataSource(postgreSQLContainer)
            repository = ProfileringRepository(dataSource)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            postgreSQLContainer.stop()
        }
    }

    @Test
    fun `opprett skal sette inn en nytt profileringsoppføring`() {
        val profileringsEntity = ProfileringTestData.arbeidssokerProfilert.tilProfileringEntity()
        val resultat = repository.opprett(profileringsEntity)

        assertEquals(1, resultat)
    }

    @Test
    fun `hentSiste skal returnere den siste profilingsoppføringen`() {
        val profileringsEntity = ProfileringTestData.arbeidssokerProfilert.tilProfileringEntity()
        repository.opprett(profileringsEntity)

        val sisteProfilering = repository.hentSiste(ProfileringTestData.foedselsnummer)

        assertEquals(ProfileringTestData.profilering.foreslattInnsatsgruppe, sisteProfilering?.foreslattInnsatsgruppe)
    }
}
