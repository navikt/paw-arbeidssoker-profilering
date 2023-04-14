package no.nav.paw.domain.db

import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe

data class ProfileringEntity(
    val foedselsnummer: Foedselsnummer,
    val registreringsId: Int,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteMnd: Boolean,
    val foreslattInnsatsgruppe: Innsatsgruppe
)
