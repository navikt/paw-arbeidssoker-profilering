package no.nav.paw

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import no.nav.paw.routes.internalRoutes
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun `Interne ruter responderer`() = testApplication {
        routing { internalRoutes() }
        val response = client.get("/internal/isAlive")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("ALIVE", response.bodyAsText())
    }
}
