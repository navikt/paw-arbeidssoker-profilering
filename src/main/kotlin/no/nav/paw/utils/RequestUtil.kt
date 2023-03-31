package no.nav.paw.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.plugins.StatusException
import no.nav.security.token.support.v2.TokenValidationContextPrincipal

fun ApplicationCall.getClaim(issuer: String, name: String): String? =
    authentication.principal<TokenValidationContextPrincipal>()
        ?.context
        ?.getClaims(issuer)
        ?.getStringClaim(name)

fun ApplicationCall.getPidClaim(): Foedselsnummer =
    getClaim("idporten", "pid")
        ?.let { Foedselsnummer(it) }
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'pid'-claim i token fra issuer")
