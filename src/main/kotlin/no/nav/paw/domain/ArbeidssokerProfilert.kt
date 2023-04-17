package no.nav.paw.domain

import no.nav.paw.profilering.ArbeidssokerProfilertEventV2

data class ArbeidssokerProfilert(
    val foedselsnummer: Foedselsnummer,
    val aktorId: AktorId,
    val registreringsId: Int,
    val profilering: Profilering
) {
    fun tilProfileringEntity() = ProfileringEntity(
        foedselsnummer,
        registreringsId,
        foedselsnummer.alder,
        profilering.jobbetSammenhengendeSeksAvTolvSisteMnd,
        profilering.foreslattInnsatsgruppe
    )
    fun tilProfileringEndringMeldingDto(id: Int) = ArbeidssokerProfilertEventV2(
        id,
        registreringsId,
        foedselsnummer.foedselsnummer,
        aktorId.aktorId,
        profilering.alder,
        profilering.jobbetSammenhengendeSeksAvTolvSisteMnd,
        profilering.foreslattInnsatsgruppe.profilertTil()
    )
}
