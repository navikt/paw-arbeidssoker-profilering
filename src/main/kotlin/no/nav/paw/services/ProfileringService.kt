package no.nav.paw.services

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.domain.beregnInnsatsgruppe
import no.nav.paw.domain.harJobbetSammenhengendeSeksAvTolvSisteManeder
import no.nav.paw.domain.slaaSammenPerioder
import no.nav.paw.domain.tilEndeligePerioder
import no.nav.paw.kafka.producers.ProfileringEndringProducer
import no.nav.paw.repository.ProfileringRepository
import no.nav.paw.utils.CallId.callId

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

        /* Mulig vi beregne et halvt årsverk i stedet?
        val etAarSiden = LocalDate.now().minusYears(1)
        val etAarsverk = 230
        val antallDagerISisteAar =
            runBlocking { aaregClient.hentArbeidsforhold(foedselsnummer.foedselsnummer, UUID.randomUUID().toString()) }
                .tilEndeligePerioder()
                .filter { it.tom >= etAarSiden }
                .sumOf { ChronoUnit.DAYS.between(it.fom, it.tom) }
        val oppfyllerKravTilArbeidserfaring = antallDagerISisteAar > (etAarsverk / 2)
         */

        val oppfyllerKravTilArbeidserfaring =
            runBlocking { aaregClient.hentArbeidsforhold(foedselsnummer.foedselsnummer, callId) }
                .tilEndeligePerioder()
                .slaaSammenPerioder()
                .harJobbetSammenhengendeSeksAvTolvSisteManeder()

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
