package services

import dao.AccessDAO
import dao.models.Access

class AuthorizationService(private val AccessDAO: AccessDAO) {
    var access: Access? = null
    fun checkResourceAccess(login: String, resource: String, role: String): Boolean {
        access = AccessDAO.getAccessByResource(login, "$resource.", role)
        return access != null
    }
}