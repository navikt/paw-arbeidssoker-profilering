package no.nav.paw.domain

import java.util.UUID

data class Profilering(
    val id: UUID? = null,
    val innsatsgruppe: Innsatsgruppe,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteManeder: Boolean
) {
    fun tilProfileringEndringMelding(foedselsnummer: Foedselsnummer) = ProfileringEndringMelding(
        id,
        foedselsnummer,
        innsatsgruppe
    )
}