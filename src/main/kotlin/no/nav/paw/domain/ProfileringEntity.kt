package no.nav.paw.domain

data class ProfileringEntity(
    val foedselsnummer: Foedselsnummer,
    val registreringsId: Int,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteMnd: Boolean,
    val foreslattInnsatsgruppe: Innsatsgruppe
)
