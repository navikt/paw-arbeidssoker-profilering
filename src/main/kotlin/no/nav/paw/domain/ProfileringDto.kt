package no.nav.paw.domain

import java.time.LocalDateTime

data class ProfileringDto(
    val id: Int? = null,
    val innsatsgruppe: Innsatsgruppe,
    val opprettet: LocalDateTime? = null,
    val endret: LocalDateTime? = null
)
