package no.nav.paw.domain

import java.time.LocalDateTime
import java.util.UUID

data class ProfileringDbo(
    val id: UUID? = null,
    val innsatsgruppe: Innsatsgruppe,
    val besvarelse: String,
    val opprettet: LocalDateTime? = null,
    val endret: LocalDateTime? = null
)
