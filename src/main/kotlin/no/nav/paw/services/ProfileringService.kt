package no.nav.paw.services

import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.aareg.Periode
import no.nav.paw.domain.Arbeidsforhold
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
import java.time.LocalDate
import java.util.UUID


class ProfileringService(
    private val profileringRepository: ProfileringRepository,
    private val profileringEndringProducer: ProfileringEndringProducer,
    private val aaregClient: AaregClient
) {
    fun opprettProfilering(arbeidssokerRegistrertMelding: ArbeidssokerRegistrert) {
        // 1. Lage profilering
        // - Alder, Jobbet sammenhengende siste x antall mnd
        // 2. Lagre profilering
        // 3. Publisere melding kafka
        // profileringRepository.opprett(foedselsnummer, profilering)
        // profileringEndringProducer.publish(profilering.tilProfileringEndringMelding(foedselsnummer))
    }

    fun hentSisteProfilering(foedselsnummer: Foedselsnummer) {
        profileringRepository.hentSiste(foedselsnummer)
    }

    private fun profilerBruker(fnr: String) {
        val arbeidsforhold = runBlocking { aaregClient.hentArbeidsforhold(fnr, UUID.randomUUID().toString()) }
            .map { it.ansettelsesperiode.periode }
            .filter { it.tom !== null && it.tom!! < LocalDate.now().minusYears(1) }
            .sortedBy { it.fom }

        val harJobbetSammenhengendeSeksAvTolvSisteManeder =
            harJobbetSammenhengendeSeksAvTolvSisteManeder(arbeidsforhold, LocalDate.now())
    }

    // FOM: 2001-01-01 TOM:
    // FOM: 27-03-23 TOM: null

    // Hente ut alle periode fra det siste året
    // Finne ut hvor mange dager man har jobbet

    private fun harJobbetSammenhengendeSeksAvTolvSisteManeder(perioder: List<Periode>, dato: LocalDate) {

    }
}
