package no.nav.paw.repository

import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.ProfileringDto
import javax.sql.DataSource

class ProfileringRepository(private val dataSource: DataSource) {
    fun opprett(foedselsnummer: Foedselsnummer, profilering: Profilering, besvarelse: String): Int {
        sessionOf(dataSource, returnGeneratedKey = true).use { session ->
            val query =
                queryOf(
                    "INSERT INTO $PROFILERING_TABELL(foedselsnummer, innsatsgruppe, besvarelse) VALUES (?, ?, ?::jsonb)",
                    foedselsnummer.foedselsnummer,
                    profilering.innsatsgruppe.toString(),
                    besvarelse
                ).asUpdate
            return session.run(query)
        }
    }

    fun hentSiste(foedselsnummer: Foedselsnummer): ProfileringDto? {
        sessionOf(dataSource).use { session ->
            val query =
                queryOf(
                    "SELECT * FROM $PROFILERING_TABELL WHERE foedselsnummer = ? ORDER BY endret DESC LIMIT 1",
                    foedselsnummer.foedselsnummer
                ).map { it.tilProfilering() }.asSingle
            return session.run(query)
        }
    }

    private fun Row.tilProfilering() = ProfileringDto(
        int("id"),
        Innsatsgruppe.valueOf(string("innsatsgruppe")),
        localDateTime("opprettet"),
        localDateTime("endret")
    )

    companion object {
        const val PROFILERING_TABELL = "profilering"
    }
}
