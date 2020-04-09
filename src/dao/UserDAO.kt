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

        var user: User? = null
        val statement = dbConnection.prepareStatement(sql)
        dbConnection
        user = statement.use {

            it.setString(1, login)
            val value = it.executeQuery()

            return when {
                value.next() -> User(
                        login = value.getString("LOGIN"),
                        hash = value.getString("HASH_PASSWORD"),
                        salt = value.getString("SALT")
                )
                else -> null
            }
        }
        return user
    }
}