package fr.inativ.commons

import fr.inativ.commons.*
import org.junit.Test
import java.time.LocalDate
import java.time.Month.JANUARY
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateUtilsTest {


    @Test
    fun firstDayOfMonth() {
        assertEquals(LocalDate.of(2019, JANUARY, 20).firstDayOfMonth(), LocalDate.of(2019, JANUARY, 1))
    }

    @Test
    fun isFirstDayOfMonth() {
        assertTrue { LocalDate.of(2019, JANUARY, 1).isFirstDayOfMonth() }
    }

    @Test
    fun withDayOfMonthOrEndOfMonth() {
        assertEquals(
            LocalDate.of(2019, JANUARY, 20).withDayOfMonthOrEndOfMonth(5), LocalDate.of(2019, JANUARY, 5)
        )
        assertEquals(
            LocalDate.of(2019, JANUARY, 20).withDayOfMonthOrEndOfMonth(35), LocalDate.of(2019, JANUARY, 31)
        )
    }

    @Test
    fun lastDayOfMonth() {
        assertEquals(LocalDate.of(2019, JANUARY, 1).lastDayOfMonth(), LocalDate.of(2019, JANUARY, 31))
    }

    @Test
    fun isEndOfMonth() {
        assertTrue { LocalDate.of(2019, JANUARY, 31).isEndOfMonth() }
    }

}