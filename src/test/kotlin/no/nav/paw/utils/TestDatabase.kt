package no.nav.paw.utils

import no.nav.paw.config.Config
import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

object TestDatabase {
    private val config = Config()
    private val container = PostgreSQLContainer(DockerImageName.parse("postgres:14.4"))
        .withDatabaseName(config.database.database)
        .withUsername(config.database.username)
        .withPassword(config.database.password)

    fun setup(): PostgreSQLContainer<*> {
        if (!container.isRunning) {
            container.start()
            migrate()
        }
        return container
    }

    private fun migrate() {
        val dataSource = setupDataSource(container)
        val flyway = setupFlyway(dataSource)
        flyway.migrate()
    }

    fun setupDataSource(postgreSQLContainer: PostgreSQLContainer<*>): DataSource =
        PGSimpleDataSource().apply {
            setURL(postgreSQLContainer.jdbcUrl)
            user = postgreSQLContainer.username
            password = postgreSQLContainer.password
        }

    private fun setupFlyway(dataSource: DataSource) = Flyway(
        Flyway.configure()
            .dataSource(dataSource)
    )
}
