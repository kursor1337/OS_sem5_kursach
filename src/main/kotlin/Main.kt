import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import java.io.File

// --size option specifies
fun main(args: Array<String>) = Main().main(args)

class Main : CliktCommand() {

    val leftCars by option("-l").int().default(5)
    val rightCars by option("-r").int().default(5)
    val inputFile by option("-f")
    val timeForRide by option("-t").default("2000")

    override fun run() {
        runBlocking {
            val additionalCars = inputFile
                ?.let { File(it) }
                ?.readLines()
                ?.let { parseCars(it) }
                ?: emptyList()
            val time = parseTime(timeForRide)
            Road(leftCars, rightCars, time, additionalCars).startTraffic()
        }
    }
}

fun parseCars(lines: List<String>): List<Pair<Long, Car>> {
    return lines
        .filter { !it.trim().startsWith("//") } // убираем комменты
        .map { line ->
            line
                .split(" ")
                .let {
                    it[0].toLong() to Car(it[1].toInt(), it[2].toInt(), Direction.valueOf(it[3]))
                }
        }
}

fun parseTime(time: String): Int? {
    return if (time == "random") {
        null
    } else {
        time.toIntOrNull() ?: 2000
    }
}
