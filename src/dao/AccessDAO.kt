package dao

import java.sql.Connection

class AccessDAO(private val dbConnection: Connection) {
    fun getAccessByResource(login: String, resource: String, role: String): Boolean {
        val sql = """
            SELECT USER.LOGIN, RESOURCE.PATH, ROLE.ROLE_NAME
            FROM USER_RESOURCE 
            LEFT JOIN USER 
                ON USER_RESOURCE.USER_ID=USER.ID  
            LEFT JOIN RESOURCE 
                ON USER_RESOURCE.RESOURCE_ID=RESOURCE.ID
            LEFT JOIN ROLE 
                ON USER_RESOURCE.ROLE_ID=ROLE.ID
            WHERE USER.LOGIN=? AND RESOURCE.PATH=? AND ROLE.ROLE_NAME=?;
            """

        val statement = dbConnection.prepareStatement(sql)
        statement.setString(1, login)
        statement.setString(2, resource)
        statement.setString(3, role)
        val value = statement.executeQuery()

        return when {
            value.next() -> true
            else -> false
        }
    }
}