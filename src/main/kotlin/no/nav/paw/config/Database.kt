package no.nav.paw.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import java.time.Duration
import javax.sql.DataSource

fun createDatabaseConfig(url: String): DataSource = HikariDataSource(
    HikariConfig().apply {
        jdbcUrl = url
        maximumPoolSize = 3
        connectionTimeout = Duration.ofSeconds(30).toMillis()
        maxLifetime = Duration.ofMinutes(30).toMillis()
    }
)

fun migrateDatabase(dataSource: DataSource) {
    Flyway.configure().baselineOnMigrate(true)
        .dataSource(dataSource)
        .load()
        .migrate()
}
