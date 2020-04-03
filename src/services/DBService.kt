package services

import java.sql.Connection
import java.sql.DriverManager

class DBService {
    var connection: Connection
        private set

    init {
        val envUrl: String = System.getenv("DBURL") ?: "jdbc:h2:./db/AuthorizationApp"
        val envLogin: String = System.getenv("DBLOGIN") ?: "sa"
        val envPass: String = System.getenv("DBPASS") ?: ""
        this.connection = DriverManager.getConnection(envUrl, envLogin, envPass)
    }
}