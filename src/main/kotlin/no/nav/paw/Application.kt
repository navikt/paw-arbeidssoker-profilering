package no.nav.paw

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import javax.sql.DataSource
import kotlin.concurrent.thread
import no.nav.paw.config.Config
import no.nav.paw.config.migrateDatabase
import no.nav.paw.kafka.consumers.ArbeidssokerRegistreringConsumer
import no.nav.paw.plugins.configureAuthentication
import no.nav.paw.plugins.configureDependencyInjection
import no.nav.paw.plugins.configureHTTP
import no.nav.paw.plugins.configureLogging
import no.nav.paw.plugins.configureSerialization
import no.nav.paw.routes.apiRoutes
import no.nav.paw.routes.internalRoutes
import no.nav.paw.routes.swaggerRoutes
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val config = Config()

    // Plugins
    configureDependencyInjection(config)
    configureAuthentication(config.authentication)
    configureHTTP()
    configureLogging()
    configureSerialization()

    // Migrate database
    val dataSource by inject<DataSource>()
    migrateDatabase(dataSource)

    val arbeidssokerRegistreringConsumer by inject<ArbeidssokerRegistreringConsumer>()

    thread {
        arbeidssokerRegistreringConsumer.start()
    }

    // Routes
    routing {
        internalRoutes()
        swaggerRoutes()
        apiRoutes()
    }
}
