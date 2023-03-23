package no.nav.paw.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.services.ProfileringService
import no.nav.paw.utils.logger
import org.koin.ktor.ext.inject

fun Route.apiRoutes() {
    val profileringService: ProfileringService by inject()

    route("/api/v1") {
        get("/profilering") {
            val fnr = Foedselsnummer("111111111111")
            try {
                val profilering = profileringService.hentSisteProfilering(fnr)
                call.respond(HttpStatusCode.OK, profilering)
            } catch (error: Exception) {
                logger.error(error.message)
            }
        }
        get("/profilering/standard-innsats") {
            call.respond(HttpStatusCode.OK, "OK")
        }
    }
}
