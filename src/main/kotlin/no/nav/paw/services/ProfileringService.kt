package no.nav.paw.services

import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository

class ProfileringService(
    private val profileringRepository: ProfileringRepository,
    private val profileringEndringProducer: ProfileringEndringProducer
) {
    fun opprettProfilering(foedselsnummer: Foedselsnummer, profilering: Profilering) {
        profileringRepository.opprett(foedselsnummer, profilering)
        profileringEndringProducer.publish(profilering.tilProfileringEndringMelding(foedselsnummer))
    }

    fun hentSisteProfilering(foedselsnummer: Foedselsnummer) {
        profileringRepository.hentSiste(foedselsnummer)
    }
}
