import enum.ExitCode.SUCCESS
import mock.ResoursesMock
import mock.SessionMock
import mock.UsersMock
import services.*
import kotlin.system.exitProcess
import org.apache.logging.log4j.kotlin.logger

object Main {
    private val logger = logger()

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Инициализация: ${args.joinToString(" ")}" }

        val authenticationService = AuthenticationService(UsersMock().users)
        val authorizationService = AuthorizationService(ResoursesMock().resources)
        val accountingService = AccountingService(SessionMock.session)
        val businessLogic = BusinessLogic(
                authenticationService,
                authorizationService,
                accountingService,
                logger
        )
        val cmdServise = CmdServise(args)
        val cmd = cmdServise.parse()
        var status = SUCCESS
        if (cmdServise.isAuthenticationNeeded()) {
            logger.info {
                "Попытка аутентифиции пользователя: " +
                        "${cmd.login!!} с паролем ${cmd.password!!}"
            }
            status = businessLogic.authentication(cmd.login!!, cmd.password!!)

            logger.info {
                "Результат шага: ${status.name}"
            }
        }
        if (status == SUCCESS && cmdServise.isAuthorizationNeeded()) {
            logger.info {
                "Попытка авторизации пользователя к ресурсу: " +
                        "${cmd.resource!!} c ролью - ${cmd.role!!}"
            }

            status = businessLogic.authorization(cmd.login!!, cmd.role!!, cmd.resource!!)

            logger.info {
                "Результат шага: ${status.name}"
            }
        }
        if (status == SUCCESS && cmdServise.isAccountingNeeded()) {
            logger.info {
                "Попытка аккаунтинга: к ресурсу ${cmd.dateStart!!} - ${cmd.dateEnd!!}, " +
                        "потребляемый объем ${cmd.volume!!}"
            }

            status = businessLogic.accounting(
                    cmd.login!!,
                    cmd.resource!!,
                    cmd.role!!,
                    cmd.dateStart!!,
                    cmd.dateEnd!!,
                    cmd.volume!!
            )

            logger.info {
                "Результат шага: ${status.name}"
            }
        }

        logger.info {
            "Завершение программы с кодом: ${status.codeNumber}" +
                    "\n---------"
        }

        exitProcess(status.codeNumber)
    }
}