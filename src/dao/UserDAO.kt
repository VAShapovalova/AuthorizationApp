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
        val user =  when {
            value.next() -> User(
                    login = value.getString("LOGIN"),
                    hash = value.getString("HASH_PASSWORD"),
                    salt = value.getString("SALT")
            )
            else -> null
        }
        statement.close()
        value.close()
        return user
    }
}