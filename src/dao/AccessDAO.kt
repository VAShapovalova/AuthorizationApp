package dao

import dao.models.Access
import java.sql.Connection

class AccessDAO(private val dbConnection: Connection) {

    fun requestAccessByResource(login: String, resource: String, role: String): Access? {
        val sql = """
            SELECT USER_RESOURCE.USER_ID, USER_RESOURCE.RESOURCE, USER_RESOURCE.ROLE, USER_RESOURCE.ID
            FROM USER_RESOURCE 
            LEFT JOIN USER 
                ON USER_RESOURCE.USER_ID=USER.ID  
            WHERE 
                USER.LOGIN=? 
            AND USER_RESOURCE.RESOURCE=SUBSTRING(?,1,LENGTH(USER_RESOURCE.RESOURCE)) 
            AND USER_RESOURCE.ROLE=?;
            """

        val statement = dbConnection.prepareStatement(sql)
        statement.setString(1, login)
        statement.setString(2, resource)
        statement.setString(3, role)
        val value = statement.executeQuery()

        return when {
            value.next() -> Access(
                    id = value.getInt("ID"),
                    userId = value.getInt("USER_ID"),
                    resource = value.getString("RESOURCE"),
                    role = value.getString("ROLE")
            )
            else -> null
        }
    }
}