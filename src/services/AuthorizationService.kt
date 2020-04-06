package services

import dao.AccessResourceDAO


class AuthorizationService(
        private val dao: AccessResourceDAO
) {
    fun isParentHaveAccess(resource: String, user: String, role: String) = dao.haveAccess(resource, user, role)
}