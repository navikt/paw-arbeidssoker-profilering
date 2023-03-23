package no.nav.paw.domain

import java.time.LocalDateTime
import java.util.UUID

data class Profilering(
    val id: UUID? = null,
    val innsatsgruppe: Innsatsgruppe,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteManeder: Boolean,
    val opprettet: LocalDateTime? = null,
    val endret: LocalDateTime? = null
) {
    fun tilProfileringEndringMelding(foedselsnummer: Foedselsnummer) = ProfileringEndringMelding(
        id,
        foedselsnummer,
        innsatsgruppe
    )
}
