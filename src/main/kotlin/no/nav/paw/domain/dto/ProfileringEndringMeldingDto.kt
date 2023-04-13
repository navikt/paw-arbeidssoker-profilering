package no.nav.paw.domain

data class ProfileringEndringMeldingDto(
    val id: Int?,
    val foedselsnummer: Foedselsnummer,
    val innsatsgruppe: Innsatsgruppe
)
