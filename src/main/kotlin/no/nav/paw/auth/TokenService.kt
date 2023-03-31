package no.nav.paw.auth

import no.nav.common.token_client.builder.AzureAdTokenClientBuilder
import no.nav.common.token_client.builder.TokenXTokenClientBuilder
import no.nav.paw.utils.logger

class TokenService {
    fun createMachineToMachineToken(scope: String): String {
        logger.info("Lager nytt Azure AD M2M-token")
        return aadMachineToMachineTokenClient.createMachineToMachineToken(scope)
    }

    fun exchangeTokenXToken(scope: String, token: String): String {
        logger.info("Veksler TokenX-token mot $scope")
        return tokendingsClient.exchangeOnBehalfOfToken(scope, token)
    }

    private val aadMachineToMachineTokenClient = AzureAdTokenClientBuilder.builder()
        .withNaisDefaults()
        .buildMachineToMachineTokenClient()

    private val tokendingsClient = TokenXTokenClientBuilder.builder()
        .withNaisDefaults()
        .buildOnBehalfOfTokenClient()
}
