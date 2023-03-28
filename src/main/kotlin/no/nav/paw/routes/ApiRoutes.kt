package no.nav.paw.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.logger
import org.koin.ktor.ext.inject

fun Route.apiRoutes() {
    val profileringService: ProfileringService by inject()
    // TODO: Endre denne til å hente pid fra token
    val fnr = Foedselsnummer("18908396568")

    route("/api/v1") {
        get("/profilering") {
            try {
                logger.info("Henter siste profilering for bruker")

                val profilering: ProfileringDto = profileringService.hentSisteProfilering(fnr)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Arbeidssøker ikke funnet")

                call.respond(HttpStatusCode.OK, profilering)
            } catch (error: Exception) {
                logger.error(error.message)
            }
        }
        get("/profilering/standard-innsats") {
            logger.info("Henter standard-innsats for bruker")

            try {
                val profilering: ProfileringDto = profileringService.hentSisteProfilering(fnr)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "Arbeidssøker ikke funnet")

                call.respond(HttpStatusCode.OK, profilering.innsatsgruppe == Innsatsgruppe.STANDARD_INNSATS)
            } catch (error: Exception) {
                logger.error(error.message)
            }
        }
    }
}
