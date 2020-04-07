import dao.AccessDAO
import dao.SessionDAO
import dao.UserDAO
import enum.ExitCode.SUCCESS
import org.apache.logging.log4j.kotlin.logger
import services.*
import kotlin.system.exitProcess

object Main {
    private val logger = logger()

    @JvmStatic
    fun main(args: Array<String>) {
        logger.info { "Инициализация: ${args.joinToString(" ")}" }
        val cmdService = CmdService(args)
        val cmd = cmdService.parse()
        var status = SUCCESS
        val dbService = DBService(logger)
        dbService.migrate()
        dbService.connect()
        val connection = dbService.connection

        if (connection != null) {

            val authenticationService = AuthenticationService(
                    UserDAO(connection)
            )
            val authorizationService = AuthorizationService(
                    AccessDAO(connection)
            )
            val accountingService = AccountingService(
                    SessionDAO(connection)
            )
            val businessLogic = BusinessLogic(
                    authenticationService,
                    authorizationService,
                    accountingService,
                    logger
            )

            if (cmdService.isAuthenticationNeeded()) {
                logger.info {
                    "Попытка аутентифиции пользователя: " +
                            "${cmd.login!!} с паролем ${cmd.password!!}"
                }
                status = businessLogic.authentication(cmd.login!!, cmd.password!!)

                logger.info {
                    "Результат шага: ${status.name}"
                }
            }
            if (status == SUCCESS && cmdService.isAuthorizationNeeded()) {
                logger.info {
                    "Попытка авторизации пользователя к ресурсу: " +
                            "${cmd.resource!!} c ролью - ${cmd.role!!}"
                }

                status = businessLogic.authorization(cmd.login!!, cmd.role!!, cmd.resource!!)

                logger.info {
                    "Результат шага: ${status.name}"
                }
            }
            if (status == SUCCESS && cmdService.isAccountingNeeded()) {
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
        }

        dbService.disconnect()
        logger.info {
            "Завершение программы с кодом: ${status.codeNumber}" +
                    "\n---------"
        }
        exitProcess(status.codeNumber)
    }
}