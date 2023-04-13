package no.nav.paw.domain

import java.time.ZonedDateTime

data class ArbeidssokerRegistrert(
    val foedselsnummer: Foedselsnummer,
    val aktorId: AktorId,
    val registringsId: Long,
    val besvarelse: Besvarelse,
    val opprettetDato: ZonedDateTime
)

data class AktorId(val aktorId: String)
data class Besvarelse(
    val utdanning: UtdanningSvar? = null,
    val utdanningBestatt: UtdanningBestattSvar? = null,
    val utdanningGodkjent: UtdanningGodkjentSvar? = null,
    val helseHinder: HelseHinderSvar? = null,
    val andreForhold: AndreForholdSvar? = null,
    val sisteStilling: SisteStillingSvar? = null,
    val dinSituasjon: DinSituasjonSvar? = null,
    val fremtidigSituasjon: FremtidigSituasjonSvar? = null,
    val tilbakeIArbeid: TilbakeIArbeidSvar? = null
)

enum class UtdanningSvar {
    INGEN_UTDANNING,
    GRUNNSKOLE,
    VIDEREGAENDE_GRUNNUTDANNING,
    VIDEREGAENDE_FAGBREV_SVENNEBREV,
    HOYERE_UTDANNING_1_TIL_4,
    HOYERE_UTDANNING_5_ELLER_MER,
    INGEN_SVAR;
}

enum class UtdanningBestattSvar {
    JA, NEI, INGEN_SVAR
}

enum class UtdanningGodkjentSvar {
    JA, NEI, VET_IKKE, INGEN_SVAR
}

enum class HelseHinderSvar {
    JA, NEI, INGEN_SVAR
}

enum class AndreForholdSvar {
    JA, NEI, INGEN_SVAR
}

enum class SisteStillingSvar {
    HAR_HATT_JOBB, HAR_IKKE_HATT_JOBB, INGEN_SVAR
}

enum class DinSituasjonSvar {
    MISTET_JOBBEN,
    ALDRI_HATT_JOBB,
    HAR_SAGT_OPP,
    VIL_BYTTE_JOBB,
    ER_PERMITTERT,
    USIKKER_JOBBSITUASJON,
    JOBB_OVER_2_AAR,
    VIL_FORTSETTE_I_JOBB,
    AKKURAT_FULLFORT_UTDANNING,
    DELTIDSJOBB_VIL_MER
}

enum class FremtidigSituasjonSvar {
    SAMME_ARBEIDSGIVER,
    SAMME_ARBEIDSGIVER_NY_STILLING,
    NY_ARBEIDSGIVER,
    USIKKER,
    INGEN_PASSER;
}

enum class TilbakeIArbeidSvar {
    JA_FULL_STILLING,
    JA_REDUSERT_STILLING,
    USIKKER,
    NEI
}
