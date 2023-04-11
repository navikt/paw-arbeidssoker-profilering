package no.nav.paw.services

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.domain.beregnInnsatsgruppe
import no.nav.paw.domain.tilEndeligePerioder
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

class ProfileringService(
    private val profileringRepository: ProfileringRepository,
    private val profileringEndringProducer: ProfileringEndringProducer,
    private val aaregClient: AaregClient,
    private val objectMapper: ObjectMapper
) {
    fun opprettProfilering(arbeidssokerRegistrertMelding: ArbeidssokerRegistrert) {
        val (foedselsnummer, _, besvarelse) = arbeidssokerRegistrertMelding
        val profilering = profilerBruker(arbeidssokerRegistrertMelding)
        profileringRepository.opprett(foedselsnummer, profilering, objectMapper.writeValueAsString(besvarelse))
        profileringEndringProducer.publish(profilering.tilProfileringEndringMelding(foedselsnummer))
    }

    fun hentSisteProfilering(foedselsnummer: Foedselsnummer): ProfileringDto? =
        profileringRepository.hentSiste(foedselsnummer)

    private fun profilerBruker(arbeidssokerRegistrertMelding: ArbeidssokerRegistrert): Profilering {
        val (foedselsnummer) = arbeidssokerRegistrertMelding

        val etAarSiden = LocalDate.now().minusYears(1)
        val etAarsverk = 230

        val antallDagerISisteAar =
            runBlocking { aaregClient.hentArbeidsforhold(foedselsnummer.foedselsnummer, UUID.randomUUID().toString()) }
                .tilEndeligePerioder()
                .filter { it.tom >= etAarSiden }
                .sumOf { ChronoUnit.DAYS.between(it.fom, it.tom) }

        /* Mulig vi skal benytte denne i stedet?
        val oppfyllerKravTilArbeidserfaring =
            runBlocking { aaregClient.hentArbeidsforhold(foedselsnummer.verdi, UUID.randomUUID().toString()) }
                .tilEndeligePerioder()
                .slaaSammenPerioder()
                .harJobbetSammenhengendeSeksAvTolvSisteManeder()
         */

        val oppfyllerKravTilArbeidserfaring = antallDagerISisteAar > (etAarsverk / 2)
        val alder = foedselsnummer.alder

        val innsatsgruppe =
            beregnInnsatsgruppe(arbeidssokerRegistrertMelding.besvarelse, alder, oppfyllerKravTilArbeidserfaring)

        return Profilering(
            null,
            innsatsgruppe,
            alder,
            oppfyllerKravTilArbeidserfaring
        )
    }
}
