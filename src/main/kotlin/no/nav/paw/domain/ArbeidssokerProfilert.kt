package no.nav.paw.domain

import no.nav.paw.profilering.ArbeidssokerProfilertEventV2
import java.time.ZonedDateTime

data class ArbeidssokerProfilert(
    val foedselsnummer: Foedselsnummer,
    val aktorId: AktorId,
    val registreringsId: Int,
    val profilering: Profilering,
    val opprettetDato: ZonedDateTime
) {
    fun tilProfileringEntity() = ProfileringEntity(
        foedselsnummer,
        registreringsId,
        foedselsnummer.alder,
        profilering.jobbetSammenhengendeSeksAvTolvSisteMnd,
        profilering.foreslattInnsatsgruppe
    )

    fun tilProfileringEndringMeldingDto(id: Long?) = ArbeidssokerProfilertEventV2(
        id?.toInt(),
        registreringsId,
        foedselsnummer.foedselsnummer,
        aktorId.aktorId,
        profilering.alder,
        profilering.jobbetSammenhengendeSeksAvTolvSisteMnd,
        profilering.foreslattInnsatsgruppe.profilertTil(),
        opprettetDato.toInstant()
    )
}
