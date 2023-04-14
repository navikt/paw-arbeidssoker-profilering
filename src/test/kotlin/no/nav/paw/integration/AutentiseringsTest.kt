package no.nav.paw.integration

import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import no.nav.paw.ProfileringTestData
import no.nav.paw.config.Config
import no.nav.paw.createConfig
import no.nav.paw.routes.apiRoutes
import no.nav.paw.utils.TestDatabase
import no.nav.paw.utils.TestKafka
import no.nav.paw.withTestApplication
import no.nav.security.mock.oauth2.MockOAuth2Server
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class AutentiseringsTest {

    companion object {
        private lateinit var postgreSQLContainer: PostgreSQLContainer<*>
        private lateinit var kafkaContainer: KafkaContainer
        private lateinit var oAuth2Server: MockOAuth2Server
        private lateinit var config: Config

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            oAuth2Server = MockOAuth2Server().also { it.start() }
            postgreSQLContainer = TestDatabase.setup()
            kafkaContainer = TestKafka.setup()
            config = createConfig(oAuth2Server, postgreSQLContainer, kafkaContainer)
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            oAuth2Server.shutdown()
            postgreSQLContainer.stop()
            kafkaContainer.stop()
        }
    }

    @Test
    fun `skal respondere med 'NoContent'`() = withTestApplication(config) {
        routing { apiRoutes() }

        val token = oAuth2Server.issueToken(
            issuerId = "default",
            claims = mapOf(
                "pid" to ProfileringTestData.foedselsnummer.foedselsnummer,
                "acr" to "Level3"
            )
        )

        val response = client.get("/api/v1/profilering") {
            bearerAuth(token.serialize())
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `skal støtte tokenx`() = withTestApplication(config) {
        routing { apiRoutes() }

        val token = oAuth2Server.issueToken(
            issuerId = "default",
            audience = "dev-gcp:paw:paw-arbeidssoker-profilering",
            claims = mapOf(
                "pid" to ProfileringTestData.foedselsnummer.foedselsnummer
            )
        )

        val response = client.get("/api/v1/profilering") {
            bearerAuth(token.serialize())
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }

    @Test
    fun `skal respondere med 401 når token har feil issuer`() = withTestApplication(config) {
        routing { apiRoutes() }

        val token = oAuth2Server.issueToken(
            issuerId = "feil",
            claims = mapOf(
                "pid" to ProfileringTestData.foedselsnummer.foedselsnummer,
                "acr" to "Level3"
            )
        )

        val response = client.get("/api/v1/profilering") {
            bearerAuth(token.serialize())
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `skal respondere med 401 når token har feil audience`() = withTestApplication(config) {
        routing { apiRoutes() }

        val token = oAuth2Server.issueToken(
            audience = "feil",
            claims = mapOf(
                "pid" to ProfileringTestData.foedselsnummer.foedselsnummer,
                "acr" to "Level3"
            )
        )

        val response = client.get("/api/v1/profilering") {
            bearerAuth(token.serialize())
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
