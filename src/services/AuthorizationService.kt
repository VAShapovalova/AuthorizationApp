package services

import dao.AccessDAO
import dao.models.Access

class AuthorizationService(private val accessDAO: AccessDAO) {
    fun getResourceAccess(login: String, resource: String, role: String): Access? {
        return accessDAO.requestAccessByResource(login, "$resource.", role)
    }

    fun checkResourceAccess(login: String, resource: String, role: String): Boolean {
        return getResourceAccess(login, "$resource.", role) != null
    }
}