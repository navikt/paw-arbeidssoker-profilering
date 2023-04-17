package no.nav.paw

import no.nav.paw.domain.AktorId
import no.nav.paw.domain.AndreForholdSvar
import no.nav.paw.domain.ArbeidssokerProfilert
import no.nav.paw.domain.ArbeidssokerRegistrert
import no.nav.paw.domain.Besvarelse
import no.nav.paw.domain.DinSituasjonSvar
import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.HelseHinderSvar
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.Profilering
import no.nav.paw.domain.SisteStillingSvar
import no.nav.paw.domain.UtdanningBestattSvar
import no.nav.paw.domain.UtdanningGodkjentSvar
import no.nav.paw.domain.UtdanningSvar
import java.time.ZonedDateTime

object ProfileringTestData {
    val foedselsnummer = Foedselsnummer("18908396568")

    val aktorId = AktorId("2862185140226")

    val besvarelse = Besvarelse(
        UtdanningSvar.GRUNNSKOLE,
        UtdanningBestattSvar.JA,
        UtdanningGodkjentSvar.JA,
        HelseHinderSvar.JA,
        AndreForholdSvar.JA,
        SisteStillingSvar.INGEN_SVAR,
        DinSituasjonSvar.MISTET_JOBBEN,
        null,
        null
    )

    val profilering = Profilering(Innsatsgruppe.STANDARD_INNSATS, 22, false)

    const val tomBesvarelse = "{}"

    val arbeidssokerRegistrert = ArbeidssokerRegistrert(
        foedselsnummer,
        aktorId,
        1,
        besvarelse,
        ZonedDateTime.now()
    )

    val arbeidssokerProfilert = ArbeidssokerProfilert(
        foedselsnummer,
        aktorId,
        1,
        ZonedDateTime.now(),
        profilering
    )
}
