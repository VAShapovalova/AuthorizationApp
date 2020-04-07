package dao.models

data class Access(
        val id: Int,
        val userId: Int,
        val resourceId: Int,
        val roleId: Int
)