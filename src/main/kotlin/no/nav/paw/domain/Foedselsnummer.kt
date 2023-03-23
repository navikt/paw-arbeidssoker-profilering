package no.nav.paw.domain
@JvmInline
value class Foedselsnummer(val verdi: String) {
    override fun toString(): String {
        return "*".repeat(11)
    }
}
