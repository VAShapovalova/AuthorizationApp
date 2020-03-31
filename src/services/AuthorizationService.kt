package services

import domain.Resources


class AuthorizationService(private val resources: List<Resources>) {

    fun checkResourceAccess(login: String, res: String, role: String) =
        resources.find { it.user == login && it.resource == res && it.role == role } != null


    fun isParentHaveAccess(resource: String, user: String, role: String): Boolean {
        val resourcesByUserAndRole = resources.filter { it.user == user && it.role == role }.map { it.resource }
        val pathArray = resource.split(".")
        var isAccessExist = false
        for (pathArrayIndex in pathArray.indices) {
            for (index in resourcesByUserAndRole.indices) {
                val isResourceEqual = resourcesByUserAndRole[index].split(".") == pathArray.slice(0..pathArrayIndex)
                if (isResourceEqual) {
                    isAccessExist = true
                }
            }
        }
        return isAccessExist
    }
}