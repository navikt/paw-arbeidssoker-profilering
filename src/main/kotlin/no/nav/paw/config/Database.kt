package no.nav.paw.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun createDatabaseConfig(url: String): DataSource = HikariDataSource(
    HikariConfig().apply {
        jdbcUrl = url
        maximumPoolSize = 3
        minimumIdle = 1
        idleTimeout = 10001
        connectionTimeout = 2000
        maxLifetime = 30001
        driverClassName = "org.postgresql.Driver"
        poolName = "defaultPool"
    }
)

fun migrateDatabase(dataSource: DataSource) {
    Flyway.configure().baselineOnMigrate(true)
        .dataSource(dataSource)
        .load()
        .migrate()
}
