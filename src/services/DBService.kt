package services

import org.flywaydb.core.Flyway
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class DBService {
    private val envUrl: String = System.getenv("DBURL") ?: "jdbc:h2:file:"
    private val envDBFile: String = System.getenv("DBFILE") ?: "./AuthorizationApp"
    private val envDBFileType: String = System.getenv("DBFILETYPE") ?: "mv.db"
    private val envLogin: String = System.getenv("DBLOGIN") ?: "sa"
    private val envPass: String = System.getenv("DBPASS") ?: ""
    private val migrationPath: String = "filesystem:db"

    var connection: Connection? = null

    init {
        connect()
        migrate()
    }

    fun connect() {
        connection = DriverManager.getConnection(envUrl, envLogin, envPass)
    }

    fun <R> inConnect(body: (db: Connection) -> R): R? {
        connect()
        if (connection != null) {
            val res = body(this.connection!!)
            disconnect()
            return res
        }
        disconnect()
        return null
    }

    fun disconnect() {
        if (connection != null) {
            connection!!.close()
        }
    }

    private fun migrate() {
        val flyway = Flyway
                .configure()
                .dataSource(envUrl + envDBFile, envLogin, envPass)
                .locations(migrationPath)
                .load()

        val dbFileName = "$envDBFile.$envDBFileType"
        if (!File(dbFileName).exists()) {
            flyway.migrate()
        }
    }

    //fun migrate() = Flyway.configure().dataSource(envUrl, envLogin, envPass).locations(migrationPath).load().migrate()
}