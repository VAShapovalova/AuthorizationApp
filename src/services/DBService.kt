package services

import org.flywaydb.core.Flyway
import java.sql.Connection
import java.sql.DriverManager

class DBService() {
    val envUrl: String = System.getenv("DBURL") ?: "jdbc:h2:./db/AuthorizationApp"
    val envLogin: String = System.getenv("DBLOGIN") ?: "sa"
    val envPass: String = System.getenv("DBPASS") ?: ""
    val migrationPath: String = "filesystem:db"
    var connection: Connection? = null

    fun connect() {
        connection = DriverManager.getConnection(envUrl, envLogin, envPass)
    }

    fun disconnect() {
        if (connection != null) {
            connection!!.close()
        }
    }

    fun migrate() = Flyway.configure().dataSource(envUrl, envLogin, envPass).locations(migrationPath).load().migrate()
}