package services

import dao.UserDAO
import java.security.MessageDigest

class AuthenticationService(
        private val userDAO: UserDAO
) {
    fun validateLogin(log: String): Boolean {
        val regex = "[a-z]{1,9}".toRegex()
        return (regex.matches(log))
    }

    fun findUserLogin(log: String) = userDAO.getUserByLogin(log) != null

    fun verificationPassword(log: String, pass: String): Boolean {
        val user = userDAO.getUserByLogin(log)
        return if (user == null) {
            false
        } else {
            user.hash == getHash(pass, user.salt)
        }
    }

    private fun hash(s: String): String {
        val bytes = s.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun getHash(pass: String, salt: String) = hash(pass + salt)

}