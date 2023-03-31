package no.nav.paw.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.getPidClaim
import no.nav.paw.utils.logger
import org.koin.ktor.ext.inject

fun Route.apiRoutes() {
    val profileringService: ProfileringService by inject()

    authenticate("idporten") {
        route("/api/v1") {
            get("/profilering") {
                try {
                    logger.info("Henter siste profilering for bruker")
                    val foedselsnummer = call.getPidClaim()

                    val profilering: ProfileringDto = profileringService.hentSisteProfilering(foedselsnummer)
                        ?: return@get call.respond(HttpStatusCode.NoContent)

                    call.respond(HttpStatusCode.OK, profilering)
                } catch (error: Exception) {
                    logger.error(error.message)
                }
            }
            get("/profilering/standard-innsats") {
                logger.info("Henter standard-innsats for bruker")
                val foedselsnummer = call.getPidClaim()

                try {
                    val profilering: ProfileringDto = profileringService.hentSisteProfilering(foedselsnummer)
                        ?: return@get call.respond(HttpStatusCode.NoContent)

                    call.respond(HttpStatusCode.OK, profilering.innsatsgruppe == Innsatsgruppe.STANDARD_INNSATS)
                } catch (error: Exception) {
                    logger.error(error.message)
                }
            }
        }
    }
}
