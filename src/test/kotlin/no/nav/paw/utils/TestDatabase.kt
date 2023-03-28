package no.nav.paw.utils

import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import javax.sql.DataSource

object TestDatabase {
    private val container = PostgreSQLContainer(DockerImageName.parse("postgres:14.4"))

    fun setup(): DataSource {
        if (!container.isRunning) {
            container.start()
        }

        val dataSource = setupDataSource(container)
        val flyway = setupFlyway(dataSource)
        flyway.migrate()

        return setupDataSource(container)
    }

    private fun setupDataSource(postgreSQLContainer: PostgreSQLContainer<*>): DataSource {
        return PGSimpleDataSource().apply {
            setURL(postgreSQLContainer.jdbcUrl)
            user = postgreSQLContainer.username
            password = postgreSQLContainer.password
        }
    }

    private fun setupFlyway(dataSource: DataSource) = Flyway(
        Flyway.configure()
            .dataSource(dataSource)
    )
}
