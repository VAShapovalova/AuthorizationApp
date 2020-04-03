package services

import enum.ExitCode
import enum.ExitCode.*
import enum.Roles
import enum.findRoles

class BusinessLogic(
        private val authenticationService: AuthenticationService,
        private val authorizationService: AuthorizationService,
        private val accountingService: AccountingService
) {
    fun authentication(login: String, pass: String): ExitCode {
        val isLoginValidated: Boolean = authenticationService.validateLogin(login)
        println("Результат валидации логина: $isLoginValidated")
        val isLoginExist: Boolean
        val isPasswordVerificated: Boolean
        if (isLoginValidated) {
            isLoginExist = authenticationService.findUserLogin(login)
            println("Результат поиска пользователя: $isLoginExist")
        } else {
            println("Невалидный формат логина")
            return INVALID_LOGIN
        }
        if (isLoginExist) {
            isPasswordVerificated = authenticationService.verificationPassword(login, pass)
            println("Результат верификации пользователя с паролем: $isPasswordVerificated")
        } else {
            println("Неизвестный пользователь")
            return UNKNOWN_LOGIN
        }
        return if (isPasswordVerificated) {
            SUCCESS
        } else {
            println("Неправильный пароль")
            INVALID_PASSWORD
        }
    }

    fun authorization(login: String, role: String, resource: String): ExitCode {
        val isRoleExist = findRoles(role)
        println("Результат валидации роли: $isRoleExist")
        val isChildAccessExist: Boolean
        var isParentAccessExist = false
        if (isRoleExist) {
            isChildAccessExist = authorizationService.checkResourceAccess(login, resource, role)
        } else {
            print("Не известная роль")
            return UNKNOWN_ROLE
        }
        if (!isChildAccessExist) {
            isParentAccessExist = authorizationService.isParentHaveAccess(resource, login, role)
        }
        val isAccessExist = isChildAccessExist || isParentAccessExist
        println("Результат верификации доступа к ресурсу: $isAccessExist")
        return if (isAccessExist) {
            SUCCESS
        } else {
            println("Нет доступа")
            FORBIDDEN
        }
    }

    fun accounting(login: String, resource: String, role: String, ds: String, de: String, vol: String): ExitCode {
        val dateStarted = accountingService.parseDate(ds)
        val dateEnd = accountingService.parseDate(de)
        val isDateValided = dateStarted != null && dateEnd != null && dateStarted.compareTo(dateEnd) == -1
        println("Результат валидации даты начала и конца: $isDateValided")
        val isVolumeValided = accountingService.validateVolume(vol)
        println("Результат валидации объема: $isVolumeValided")
        return if (isDateValided && isVolumeValided) {
            accountingService.addNewSession(login, resource, Roles.valueOf(role), ds, de, vol.toInt())
            SUCCESS
        } else {
            println("Неверная активность")
            INCORRECT_ACTIVITY
        }
    }
}