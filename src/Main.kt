import dao.AccessDAO
import dao.UserDAO
import enum.ExitCode.SUCCESS
import mock.SessionMock
import services.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val cmdServise = CmdServise(args)
    val cmd = cmdServise.parse()
    var status = SUCCESS
    val dbService = DBService()
    dbService.migrate()
    dbService.connect()
    if (dbService.connection != null) {
        val authenticationService = AuthenticationService(
                UserDAO(dbService.connection!!)
        )
        val authorizationService = AuthorizationService(AccessDAO(dbService.connection!!))
        val accountingService = AccountingService(SessionMock.session)
        val businessLogic = BusinessLogic(authenticationService, authorizationService, accountingService)

        if (cmdServise.isAuthenticationNeeded()) {
            status = businessLogic.authentication(cmd.login!!, cmd.password!!)
        }
        if (status == SUCCESS && cmdServise.isAuthorizationNeeded()) {
            status = businessLogic.authorization(cmd.login!!, cmd.role!!, cmd.resource!!)
        }
        if (status == SUCCESS && cmdServise.isAccountingNeeded()) {
            status = businessLogic.accounting(
                    cmd.login!!,
                    cmd.resource!!,
                    cmd.role!!,
                    cmd.dateStart!!,
                    cmd.dateEnd!!,
                    cmd.volume!!
            )
        }
    }
    dbService.disconnect()
    exitProcess(status.codeNumber)
}