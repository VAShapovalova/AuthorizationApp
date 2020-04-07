package services

import domain.Session
import enum.ExitCode
import enum.ExitCode.*
import enum.Roles
import enum.findRoles
import org.apache.logging.log4j.kotlin.KotlinLogger

class BusinessLogic(
        private val authenticationService: AuthenticationService,
        private val authorizationService: AuthorizationService,
        private val accountingService: AccountingService,
        private val logger: KotlinLogger
) {
    fun authentication(login: String, pass: String): ExitCode {
        val isLoginValidated: Boolean = authenticationService.validateLogin(login)

        logger.info { "Результат валидации логина: $isLoginValidated" }

        val isLoginExist: Boolean
        val isPasswordVerificated: Boolean
        if (isLoginValidated) {
            isLoginExist = authenticationService.findUserLogin(login)
            logger.info { "Результат поиска пользователя: $isLoginExist" }
        } else {
            logger.error { "Невалидный формат логина" }
            return INVALID_LOGIN
        }
        if (isLoginExist) {
            isPasswordVerificated = authenticationService.verificationPassword(login, pass)
            logger.info { "Результат верификации пользователя с паролем: $isPasswordVerificated" }
        } else {
            logger.error { "Неизвестный пользователь" }
            return UNKNOWN_LOGIN
        }
        return if (isPasswordVerificated) {
            SUCCESS
        } else {
            logger.error { "Неправильный пароль" }
            INVALID_PASSWORD
        }
    }

    fun authorization(login: String, role: String, resource: String): ExitCode {
        val isRoleExist = findRoles(role)
        logger.info { "Результат валидации роли: $isRoleExist" }
        val isAccessExist: Boolean
        if (isRoleExist) {
            isAccessExist = authorizationService.checkResourceAccess(login, resource, role)
        } else {
            logger.error { "Не известная роль" }
            return UNKNOWN_ROLE
        }
        logger.info { "Результат верификации доступа к ресурсу: $isAccessExist" }
        return if (isAccessExist) {
            SUCCESS
        } else {
            logger.error { "Нет доступа" }
            FORBIDDEN
        }
    }

    fun accounting(login: String, resource: String, role: String, ds: String, de: String, vol: String): ExitCode {
        val dateStarted = accountingService.parseDate(ds)
        val dateEnd = accountingService.parseDate(de)
        val isDateValided = dateStarted != null && dateEnd != null && dateStarted.compareTo(dateEnd) == -1
        logger.info { "Результат валидации даты начала и конца: $isDateValided" }
        val isVolumeValided = accountingService.validateVolume(vol)
        logger.info { "Результат валидации объема: $isVolumeValided" }
        val access = authorizationService.getResourceAccess(login, resource, role)
        return if (isDateValided && isVolumeValided && access != null) {
            val session = Session(login, Roles.valueOf(role), resource, ds, de, vol.toInt())
            accountingService.addNewSession(access, session)
            SUCCESS
        } else {
            logger.error { "Неверная активность" }
            INCORRECT_ACTIVITY
        }
    }
}