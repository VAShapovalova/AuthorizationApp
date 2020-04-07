package services

import dao.AccessDAO
import dao.models.Access

class AuthorizationService(private val AccessDAO: AccessDAO) {
    var access: Access? = null
    fun checkResourceAccess(login: String, resource: String, role: String): Boolean {
        val pathArray = resource.split(".")
        var isAccessExist = false
        for (pathArrayIndex in pathArray.indices) {
            val resource = pathArray.slice(0..pathArrayIndex).joinToString(".")
            access = AccessDAO.getAccessByResource(login, resource, role)
            if (access != null) {
                isAccessExist = true
            }
        }
        return isAccessExist
    }
}