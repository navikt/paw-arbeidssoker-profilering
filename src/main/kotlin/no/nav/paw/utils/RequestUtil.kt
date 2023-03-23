package no.nav.paw.utils

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.authentication
import io.ktor.server.auth.parseAuthorizationHeader
import io.ktor.server.request.header
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.plugins.StatusException
import no.nav.security.token.support.v2.TokenValidationContextPrincipal
import java.util.UUID

fun ApplicationCall.getClaim(issuer: String, name: String): String? =
    authentication.principal<TokenValidationContextPrincipal>()
        ?.context
        ?.getClaims(issuer)
        ?.getStringClaim(name)

fun ApplicationCall.getPidClaim(): Foedselsnummer =
    getClaim("idporten", "pid")
        ?.let { Foedselsnummer(it) }
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'pid'-claim i token fra issuer")

fun ApplicationCall.getACR(): String =
    getClaim("idporten", "acr")
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'acr'-claim i token fra issuer")

fun ApplicationCall.getNAVIdent(): String =
    getClaim("azure", "NAVident")
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'NAVident'-claim i token fra issuer")

fun ApplicationCall.getNavAnsattAzureId(): UUID =
    getClaim("azure", "oid")
        ?.let { UUID.fromString(it) }
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'oid'-claim i token fra issuer")

fun ApplicationCall.getNorskIdent(): Foedselsnummer =
    request.header("nav-norskident")
        ?.let { Foedselsnummer(it) }
        ?: throw StatusException(HttpStatusCode.Forbidden, "Fant ikke 'nav-norskident' i header")

fun ApplicationCall.getAccessToken(): String =
    request.parseAuthorizationHeader()
        .toString()
        .replace("Bearer", "")
