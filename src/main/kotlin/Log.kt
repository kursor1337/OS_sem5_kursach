import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun log(string: String) {
    val time = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .time
        .let { "${it.hour}-${it.minute}-${it.second}-${it.nanosecond / 1_000_000}" }
    println("$time:: $string")
}