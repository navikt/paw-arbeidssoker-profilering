package no.nav.paw.repository

import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.ProfileringDbo
import javax.sql.DataSource

class ProfileringRepository(private val dataSource: DataSource) {
    fun opprett(foedselsnummer: Foedselsnummer, profilering: Profilering): Int {
        sessionOf(dataSource, returnGeneratedKey = true).use { session ->
            val query =
                queryOf(
                    "INSERT INTO $PROFILERING_TABELL(foedselsnummer, innsatsgruppe, besvarelse) VALUES (?, ?, ?, ?)",
                    foedselsnummer.verdi,
                    profilering.innsatsgruppe
                ).asUpdate
            return session.run(query)
        }
    }

    fun hentSiste(foedselsnummer: Foedselsnummer): ProfileringDbo? {
        sessionOf(dataSource).use { session ->
            val query =
                queryOf(
                    "SELECT * FROM $PROFILERING_TABELL WHERE foedselsnummer = ? ORDER BY endret DESC LIMIT 1",
                    foedselsnummer.verdi
                ).map {
                    it.tilProfilering()
                }.asSingle
            return session.run(query)
        }
    }

    private fun Row.tilProfilering() = ProfileringDbo(
        uuid("id"),
        enumValueOf("innsatsgruppe"),
        string("besvarelse"),
        localDateTime("opprettet"),
        localDateTime("endret")
    )

    companion object {
        const val PROFILERING_TABELL = "profilering"
    }
}
