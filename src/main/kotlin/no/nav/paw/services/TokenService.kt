package no.nav.paw.services

import no.nav.common.token_client.builder.AzureAdTokenClientBuilder
import no.nav.paw.utils.logger

class TokenService {
    fun createMachineToMachineToken(scope: String): String {
        logger.info("Lager nytt Azure AD M2M-token")
        return aadMachineToMachineTokenClient.createMachineToMachineToken(scope)
    }

    private val aadMachineToMachineTokenClient = AzureAdTokenClientBuilder.builder()
        .withNaisDefaults()
        .buildMachineToMachineTokenClient()
}