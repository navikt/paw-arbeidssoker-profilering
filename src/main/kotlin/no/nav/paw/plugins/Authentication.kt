package no.nav.paw.plugins

import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import no.nav.paw.config.AuthProvider
import no.nav.security.token.support.v2.IssuerConfig
import no.nav.security.token.support.v2.TokenSupportConfig
import no.nav.security.token.support.v2.tokenValidationSupport

fun Application.configureAuthentication(authProviders: List<AuthProvider>) {
    authentication {
        authProviders.forEach { authProvider ->
            tokenValidationSupport(
                name = authProvider.name,
                requiredClaims = authProvider.requiredClaims?.let { authProvider.requiredClaims },
                config = TokenSupportConfig(
                    IssuerConfig(
                        name = authProvider.name,
                        discoveryUrl = authProvider.discoveryUrl,
                        acceptedAudience = authProvider.acceptedAudience,
                        cookieName = authProvider.cookieName
                    )
                )
            )
        }
    }
}
