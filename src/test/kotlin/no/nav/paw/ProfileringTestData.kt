package no.nav.paw

import no.nav.paw.domain.Foedselsnummer
import no.nav.paw.domain.Innsatsgruppe
import no.nav.paw.domain.Profilering

object ProfileringTestData {
    val foedselsnummer = Foedselsnummer("12345678901")
    val profilering = Profilering(null, Innsatsgruppe.STANDARD_INNSATS, 22, false)
    val besvarelse = "{}"
}