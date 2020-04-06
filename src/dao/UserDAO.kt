package dao

import domain.User
import services.DBService

class UserDAO(private val db: DBService) {
    private val loginColumn = "LOGIN"
    private val hashColumn = "HASH_PASSWORD"
    private val saltColumn = "SALT"

    private val userByLoginSql = "SELECT * FROM USER WHERE LOGIN=?;"

    fun getUserByLogin(login: String): User? {
        return db.inConnect {

            val statement = it.prepareStatement(userByLoginSql)
            statement.setString(1, login)

            val value = statement.executeQuery()

            when {
                value.next() -> User(
                        value.getString(loginColumn),
                        value.getString(hashColumn),
                        value.getString(saltColumn)
                )
                else -> null
            }
        }
    }
}