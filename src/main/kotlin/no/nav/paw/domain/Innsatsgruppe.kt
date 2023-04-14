package no.nav.paw.domain

import no.nav.paw.profilering.ProfilertTil

enum class Innsatsgruppe {
    STANDARD_INNSATS,
    SITUASJONSBESTEMT_INNSATS,
    BEHOV_FOR_ARBEIDSEVNEVURDERING;

    fun profilertTil() = when (this) {
        STANDARD_INNSATS -> ProfilertTil.ANTATT_GODE_MULIGHETER
        SITUASJONSBESTEMT_INNSATS -> ProfilertTil.ANTATT_BEHOV_FOR_VEILEDNING
        BEHOV_FOR_ARBEIDSEVNEVURDERING -> ProfilertTil.OPPGITT_HINDRINGER
    }
}

fun beregnInnsatsgruppe(besvarelse: Besvarelse, alder: Int, oppfyllerKravTilArbeidserfaring: Boolean): Innsatsgruppe {
    if (besvarelse.helseHinder == HelseHinderSvar.JA || besvarelse.andreForhold == AndreForholdSvar.JA) {
        return Innsatsgruppe.BEHOV_FOR_ARBEIDSEVNEVURDERING
    }

    if (alder in 18..59 &&
        oppfyllerKravTilArbeidserfaring &&
        UtdanningSvar.INGEN_UTDANNING != besvarelse.utdanning &&
        UtdanningBestattSvar.JA == besvarelse.utdanningBestatt &&
        UtdanningGodkjentSvar.JA == besvarelse.utdanningGodkjent &&
        HelseHinderSvar.NEI == besvarelse.helseHinder &&
        AndreForholdSvar.NEI == besvarelse.andreForhold
    ) {
        return Innsatsgruppe.STANDARD_INNSATS
    }

    return Innsatsgruppe.SITUASJONSBESTEMT_INNSATS
}
