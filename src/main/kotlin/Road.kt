import kotlinx.coroutines.*
import kotlin.random.Random

class Road(
    numberOfRandomCars: Int = 10,
    private val additionalCars: List<Pair<Long, Car>>
) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val bridge = Bridge()
    private val cars = randomListOfCars(numberOfRandomCars)

    init {
        val (leftCars, rightCars) = cars
            .partition { it.direction == Direction.Left }
            .let { it.first.joinToString("\n") to it.second.joinToString("\n") }
        log(
            """
            
            |Road initialized:
            |$leftCars
            |BRIDGE
            |$rightCars
        """.trimMargin()
        )
    }

    fun startTraffic(): List<Job> {
        return cars.map { car ->
            coroutineScope.launch { bridge.passCar(car) }
        }.plus(
            additionalCars.map { (delay, car) ->
                coroutineScope.launch {
                    delay(delay)
                    bridge.passCar(car)
                }
            }
        )
    }

}

private fun randomListOfCars(size: Int = 10): List<Car> = List(size) { index ->
    Car(
        id = index,
        direction = mapPriorityToDirection(Random.nextInt(0, 2))
    )
}

private fun mapPriorityToDirection(priority: Int) = when (priority) {
    0 -> Direction.Right
    1 -> Direction.Left
    else -> error("Priority $priority not exists")
}