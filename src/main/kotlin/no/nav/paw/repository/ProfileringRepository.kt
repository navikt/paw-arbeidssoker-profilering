package no.nav.paw.repository

import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.domain.ProfileringEntity
import javax.sql.DataSource

class ProfileringRepository(private val dataSource: DataSource) {
    fun opprett(profileringEntity: ProfileringEntity): Long? {
        sessionOf(dataSource, returnGeneratedKey = true).use { session ->
            val query =
                queryOf(
                    "INSERT INTO $PROFILERING_TABELL(foedselsnummer, registrerings_id, alder, jobbet_sammenhengende_seks_av_tolv_siste_mnd, foreslatt_innsatsgruppe) VALUES (?, ?::int, ?::int, ?::boolean, ?)",
                    profileringEntity.foedselsnummer.foedselsnummer,
                    profileringEntity.registreringsId,
                    profileringEntity.alder,
                    profileringEntity.jobbetSammenhengendeSeksAvTolvSisteMnd,
                    profileringEntity.foreslattInnsatsgruppe.toString()
                ).asUpdateAndReturnGeneratedKey
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
        int("registrerings_id"),
        int("alder"),
        boolean("jobbet_sammenhengende_seks_av_tolv_siste_mnd"),
        Innsatsgruppe.valueOf(string("foreslatt_innsatsgruppe")),
        localDateTime("opprettet"),
        localDateTime("endret")
    )

    companion object {
        const val PROFILERING_TABELL = "profilering"
    }
}
