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

    val numberOfCars by option("-n").int().default(10)
    val inputFile by option("-f")

    override fun run() {
        runBlocking {
            val additionalCars = inputFile
                ?.let { File(it) }
                ?.readLines()
                ?.let { parseCars(it) }
                ?: emptyList()
            Road(numberOfCars, additionalCars).startTraffic().joinAll()
        }
    }
}

fun parseCars(lines: List<String>): List<Pair<Long, Car>> {
    return lines
        .map { line ->
            line
                .split(" ")
                .let {
                    it[0].toLong() to Car(it[1].toInt(), Direction.valueOf(it[2]))
                }
        }
}
