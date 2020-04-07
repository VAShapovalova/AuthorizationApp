package services

import org.apache.logging.log4j.kotlin.KotlinLogger
import org.flywaydb.core.Flyway
import java.sql.Connection
import java.sql.DriverManager

class DBService(private val logger: KotlinLogger) {
    val envUrl: String = System.getenv("DBURL") ?: "jdbc:h2:./db/AuthorizationApp"
    val envLogin: String = System.getenv("DBLOGIN") ?: "sa"
    val envPass: String = System.getenv("DBPASS") ?: ""
    val migrationPath: String = "filesystem:db"
    var connection: Connection? = null

    fun connect() {
        logger.info("Подключаемся к базе данных — url: $envUrl, login $envLogin")
        connection = DriverManager.getConnection(envUrl, envLogin, envPass)
    }

    fun disconnect() {
        logger.info("Отключаемся от базы данных")
        if (connection != null) {
            connection!!.close()
        }
    }

    fun migrate() {
        logger.info("Запущены миграции $migrationPath")
        Flyway.configure().dataSource(envUrl, envLogin, envPass).locations(migrationPath).load().migrate()
    }
}