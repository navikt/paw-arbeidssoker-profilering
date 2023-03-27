package no.nav.paw.services

import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
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

        val harJobbetSammenhengendeSeksAvTolvSisteManeder =
            arbeidsforhold.harJobbetSammenhengendeSeksAvTolvSisteManeder(dagensDato())
    }
}
