package dao

import domain.User
import java.sql.Connection

class UserDAO(private val dbConnection: Connection) {
    fun getUserByLogin(login: String): User? {
        val sql = """
            SELECT *
            FROM USER
            WHERE LOGIN=?;
            """

        val statement = dbConnection.prepareStatement(sql)
        statement.setString(1, login)
        val value = statement.executeQuery()

        return when {
            value.next() -> User(
                    value.getString("LOGIN"),
                    value.getString("HASH_PASSWORD"),
                    value.getString("SALT")
            )
            else -> null
        }
    }
}