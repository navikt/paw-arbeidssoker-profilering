package no.nav.paw.domain

data class Profilering(
    val foreslattInnsatsgruppe: Innsatsgruppe,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteMnd: Boolean
)
