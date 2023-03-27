package no.nav.paw.domain

import java.util.UUID

data class ProfileringEndringMelding(
    val id: UUID?,
    val foedselsnummer: Foedselsnummer,
    val innsatsgruppe: Innsatsgruppe
)