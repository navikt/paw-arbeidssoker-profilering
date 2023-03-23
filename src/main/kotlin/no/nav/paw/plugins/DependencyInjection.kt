package no.nav.paw.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import no.nav.paw.config.Config
import no.nav.paw.config.dependencyInjectionConfig
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection(config: Config) {
    install(Koin) {
        modules(
            listOf(
                dependencyInjectionConfig(config)
            )
        )
    }
}
