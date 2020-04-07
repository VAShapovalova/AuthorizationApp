import dao.AccessDAO
import dao.UserDAO
import enum.ExitCode.SUCCESS
import mock.SessionMock
import org.apache.logging.log4j.kotlin.logger
import services.*
import kotlin.system.exitProcess

object Main {
    private val logger = logger()

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Инициализация: ${args.joinToString(" ")}" }
        val cmdServise = CmdServise(args)
        val cmd = cmdServise.parse()
        var status = SUCCESS
        val dbService = DBService(logger)
        dbService.migrate()
        dbService.connect()

        if (dbService.connection != null) {

            val authenticationService = AuthenticationService(
                    UserDAO(dbService.connection!!)
            )
            val authorizationService = AuthorizationService(AccessDAO(dbService.connection!!))
            val accountingService = AccountingService(SessionMock.session)
            val businessLogic = BusinessLogic(authenticationService, authorizationService, accountingService, logger)

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

        }

        dbService.disconnect()
        exitProcess(status.codeNumber)
    }   
}