package no.nav.paw.domain

data class Profilering(
    val id: Int? = null,
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
