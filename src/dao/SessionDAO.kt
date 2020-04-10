package dao

import dao.models.Access
import domain.Session
import java.sql.Connection
import java.sql.Date

class SessionDAO(private val dbConnection: Connection) {
    fun insert(
            access: Access,
            session: Session
    ): Int {
        val sql = """
            INSERT INTO USER_SESSION(ID, ACCESS_ID, START_DATE, END_DATE, VOLUME, PATH)
            VALUES(default, ?, ?, ?, ?, ?)
            """
        val statement = dbConnection.prepareStatement(sql)
        statement.setInt(1, access.id)
        statement.setDate(2, Date.valueOf(session.ds))
        statement.setDate(3, Date.valueOf(session.de))
        statement.setInt(4, session.vol)
        statement.setString(5, session.resource)
        val result = statement.executeUpdate()
        statement.close()
        return result
    }
}
