package no.nav.paw.domain.dto

import no.nav.paw.domain.Innsatsgruppe

data class ProfileringEndringMeldingDto(
    val id: Int,
    val registreringsId: Int,
    val foedselsnummer: String,
    val aktorId: String,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteMnd: Boolean,
    val foreslattInnsatsgruppe: Innsatsgruppe
)