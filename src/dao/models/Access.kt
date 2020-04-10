package dao.models

data class Access(
        val id: Int,
        val userId: Int,
        val resource: String,
        val role: String
)