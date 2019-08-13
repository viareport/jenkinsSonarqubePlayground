package demo

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjusters

const val FORMAT_DATE = "yyyy.MM.dd"

const val YEAR_MONTH_FORMAT_DASH = "yyyy-MM"
const val YEAR_MONTH_FORMAT_SLASH = "yyyy/MM"
const val YEAR_MONTH_FORMAT_UNDESCORE = "yyyy_MM"

const val DATE_FORMAT_SLASH = "yyyy/MM/dd"

val YEAR_MONTH_FORMATTER_SLASH = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT_SLASH)
val YEAR_MONTH_FORMATTER_UNDESCORE = DateTimeFormatter.ofPattern(YEAR_MONTH_FORMAT_UNDESCORE)

fun LocalDate.firstDayOfMonth(): LocalDate = this.withDayOfMonth(1)

fun LocalDate.isFirstDayOfMonth() = this.dayOfMonth == 1

fun LocalDate.withDayOfMonthOrEndOfMonth(jour: Int): LocalDate {
    val nbJour = this.lengthOfMonth()
    return if (jour <= nbJour) {
        this.withDayOfMonth(jour)
    } else {
        this.with(TemporalAdjusters.lastDayOfMonth())
    }
}


fun LocalDate.lastDayOfMonth(): LocalDate {
    return this.with(TemporalAdjusters.lastDayOfMonth())
}

fun LocalDate?.isEndOfMonth(): Boolean {
    return this?.let { it.lastDayOfMonth() == it } ?: false
}

fun TemporalAccessor.toYearMonth() = YearMonth.from(this)

data class LocalDateRange(override val start: LocalDate, override val endInclusive: LocalDate) : ClosedRange<LocalDate>