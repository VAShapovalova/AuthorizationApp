import enum.ExitCode.SUCCESS
import mock.ResoursesMock
import mock.SessionMock
import mock.UsersMock
import services.*
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    println("Инициализация")
    val authenticationService = AuthenticationService(UsersMock().users)
    val authorizationService = AuthorizationService(ResoursesMock().resources)
    val accountingService = AccountingService(SessionMock.session)
    val businessLogic = BusinessLogic(authenticationService, authorizationService, accountingService)
    val cmdServise = CmdServise(args)
    val cmd = cmdServise.parse()
    var status = SUCCESS
    if (cmdServise.isAuthenticationNeeded()) {
        println("Аутентифицируем пользователя ${cmd.login!!} с паролем ${cmd.password!!}")
        status = businessLogic.authentication(cmd.login!!, cmd.password!!)

        println("Результат шага: ${status.name}\n---------")
    }
    if (status == SUCCESS && cmdServise.isAuthorizationNeeded()) {
        println("Авторизируем пользователя к ресурсу ${cmd.resource!!} c ролью - ${cmd.role!!}")
        status = businessLogic.authorization(cmd.login!!, cmd.role!!, cmd.resource!!)
        println("Результат шага: ${status.name}\n---------")
    }
    if (status == SUCCESS && cmdServise.isAccountingNeeded()) {
        println("Попытка аккаунтинга к ресурсу ${cmd.dateStart!!} - ${cmd.dateEnd!!}, потребляемы объем ${cmd.volume!!}")
        status = businessLogic.accounting(cmd.login!!, cmd.resource!!, cmd.role!!, cmd.dateStart!!, cmd.dateEnd!!, cmd.volume!!)
        println("Результат шага: ${status.name}\n---------")
    }
    println("Завершение программы с кодом ${status.codeNumber}")
    exitProcess(status.codeNumber)
}

