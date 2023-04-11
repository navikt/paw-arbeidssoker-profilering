package no.nav.paw

import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.Profilering

object ProfileringTestData {
    val foedselsnummer = Foedselsnummer("18908396568")
    val profilering = Profilering(null, Innsatsgruppe.STANDARD_INNSATS, 22, false)
    const val tomBesvarelse = "{}"
}
