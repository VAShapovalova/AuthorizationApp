package services

import dao.AccessDAO


class AuthorizationService(private val AccessDAO: AccessDAO) {

    fun checkResourceAccess(login: String, resource: String, role: String): Boolean {
        val pathArray = resource.split(".")
        var isAccessExist = false
        for (pathArrayIndex in pathArray.indices) {
                val isResourceEqual = AccessDAO.getAccessByResource(login, pathArray.slice(0..pathArrayIndex).joinToString("."), role)
                if (isResourceEqual) {
                    isAccessExist = true
                }
        }
        return isAccessExist
    }
}