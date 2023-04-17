package no.nav.paw.services

import kotlinx.coroutines.runBlocking
import no.nav.paw.aareg.AaregClient
import no.nav.paw.domain.ArbeidssokerProfilert
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.ProfileringDto
import no.nav.paw.domain.beregnInnsatsgruppe
import no.nav.paw.domain.harJobbetSammenhengendeSeksAvTolvSisteMnd
import no.nav.paw.domain.slaaSammenPerioder
import no.nav.paw.domain.tilEndeligePerioder
import no.nav.paw.kafka.producers.ArbeidssokerProfilertProducer
import no.nav.paw.repository.ProfileringRepository
import no.nav.paw.utils.CallId.callId

class ProfileringService(
    private val profileringRepository: ProfileringRepository,
    private val arbeidssokerProfilertProducer: ArbeidssokerProfilertProducer,
    private val aaregClient: AaregClient
) {
    fun opprettProfilering(arbeidssokerRegistrert: ArbeidssokerRegistrert) {
        val arbeidssokerProfilert = profilerBruker(arbeidssokerRegistrert)

        val profileringEntity = arbeidssokerProfilert.tilProfileringEntity()
        val id = profileringRepository.opprett(profileringEntity)

        val profileringEndringMelding = arbeidssokerProfilert.tilProfileringEndringMeldingDto(id)
        arbeidssokerProfilertProducer.publish(profileringEndringMelding)
    }

    fun hentSisteProfilering(foedselsnummer: Foedselsnummer): ProfileringDto? =
        profileringRepository.hentSiste(foedselsnummer)

    private fun profilerBruker(arbeidssokerRegistrert: ArbeidssokerRegistrert): ArbeidssokerProfilert {
        val (foedselsnummer) = arbeidssokerRegistrert

        /* Mulig vi skal beregne et halvt årsverk i stedet?
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
                .harJobbetSammenhengendeSeksAvTolvSisteMnd()

        val alder = foedselsnummer.alder

        val foreslattInnsatsgruppe =
            beregnInnsatsgruppe(arbeidssokerRegistrert.besvarelse, alder, oppfyllerKravTilArbeidserfaring)

        val profilering = Profilering(
            foreslattInnsatsgruppe,
            alder,
            oppfyllerKravTilArbeidserfaring
        )
        return ArbeidssokerProfilert(
            arbeidssokerRegistrert.foedselsnummer,
            arbeidssokerRegistrert.aktorId,
            arbeidssokerRegistrert.registreringsId,
            profilering,
            arbeidssokerRegistrert.opprettetDato
        )
    }
}
