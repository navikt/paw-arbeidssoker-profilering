package no.nav.paw.services

import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.beregnInnsatsgruppe
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*


class ProfileringService(
    private val profileringRepository: ProfileringRepository,
    private val profileringEndringProducer: ProfileringEndringProducer,
    private val aaregClient: AaregClient
) {
    fun opprettProfilering(arbeidssokerRegistrertMelding: ArbeidssokerRegistrert) {
        val foedselsnummer = arbeidssokerRegistrertMelding.foedselsnummer
        val profilering = profilerBruker(arbeidssokerRegistrertMelding)
        profileringRepository.opprett(foedselsnummer, profilering)
        profileringEndringProducer.publish(profilering.tilProfileringEndringMelding(foedselsnummer))
    }

    fun hentSisteProfilering(foedselsnummer: Foedselsnummer) {
        profileringRepository.hentSiste(foedselsnummer)
    }

    private fun profilerBruker(arbeidssokerRegistrertMelding: ArbeidssokerRegistrert): Profilering {
        val fnr = arbeidssokerRegistrertMelding.foedselsnummer

        val perioder = runBlocking { aaregClient.hentArbeidsforhold(fnr.verdi, UUID.randomUUID().toString()) }
            .map { it.ansettelsesperiode.periode }
            .filter { it.tom !== null && it.tom!! < LocalDate.now().minusYears(1) }
            .sortedBy { it.fom }

        val iDag = LocalDate.now()
        val etÅrSiden = iDag.minusYears(1)

        val antallDagerISisteÅr = perioder
            .filter { it.tom == null || it.tom!! >= etÅrSiden }
            .sumOf { ChronoUnit.DAYS.between(it.fom, it.tom ?: iDag) }

        val oppfyllerKravTilArbeidserfaring = antallDagerISisteÅr > (365/2)
        val alder = fnr.alder

        val innsatsgruppe = beregnInnsatsgruppe(arbeidssokerRegistrertMelding.besvarelse, alder, oppfyllerKravTilArbeidserfaring)

        return Profilering(
            id = null,
            innsatsgruppe,
            alder,
            oppfyllerKravTilArbeidserfaring
        )
    }

}
