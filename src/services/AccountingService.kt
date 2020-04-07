package services

import dao.SessionDAO
import dao.models.Access
import domain.Session
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class AccountingService(
        private val dao: SessionDAO
) {
    fun parseDate(dateString: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date: LocalDate?
        date = try {
            LocalDate.parse(dateString, formatter)
        } catch (ex: DateTimeParseException) {
            null
        }
        return date
    }

    fun validateVolume(volume: String) = volume.toIntOrNull() != null

    fun addNewSession(
            access: Access,
            session: Session
    ) {
        dao.insert(access, session)
    }
}