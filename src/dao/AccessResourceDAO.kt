package dao

import services.DBService

class AccessResourceDAO(private val db: DBService) {
    private val haveAccessSql = """
        SELECT RES.PATH, ROLE.ROLE_NAME
        FROM RESOURCE RES
        JOIN USER_RESOURCE UR ON RES.ID = UR.RESOURCE_ID
        JOIN ROLE ON ROLE.ID = UR.ROLE_ID
        WHERE UR.USER_ID =(
                SELECT ID
                FROM USER
                WHERE LOGIN=?
            )
            AND ROLE.ROLE_NAME=?
            AND RES.PATH=SUBSTRING(?,1,LENGTH(RES.PATH)
        )
    """.trimIndent()

    fun haveAccess(resource: String, user: String, role: String): Boolean {
        val res: Boolean? = db.inConnect {
            val statement = it.prepareStatement(haveAccessSql)
            statement.setString(1, user)
            statement.setString(2, role)
            statement.setString(3, resource)

            val value = statement.executeQuery()

            return@inConnect value.next()
        }
        return res ?: false
    }
}