package no.nav.paw.domain

data class ProfileringEndringMelding(
    val id: Int?,
    val foedselsnummer: Foedselsnummer,
    val innsatsgruppe: Innsatsgruppe
)
