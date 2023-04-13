package no.nav.paw.domain

import java.time.LocalDateTime

data class ProfileringDto(
    val id: Int,
    val registreringsId: Int,
    val alder: Int,
    val jobbetSammenhengendeSeksAvTolvSisteMnd: Boolean,
    val foreslattInnsatsgruppe: Innsatsgruppe,
    val opprettet: LocalDateTime? = null,
    val endret: LocalDateTime? = null
)
