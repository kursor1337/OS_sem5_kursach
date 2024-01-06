import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlin.random.Random

class Road(
    numberOfLeftCars: Int,
    numberOfRightCars: Int,
    timeForRide: Int?,
    private val additionalCars: List<Pair<Long, Car>>
) {

    // пул потоков
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val cars = List(numberOfLeftCars) { index ->
        Car(
            id = index,
            durationOfRide = timeForRide ?: (Random.nextInt(10, 41) * 100),
            direction = Direction.Left
        )
    }.plus(
        List(numberOfRightCars) { index ->
            Car(
                id = numberOfLeftCars + index,
                durationOfRide = timeForRide ?: (Random.nextInt(10, 41) * 100),
                direction = Direction.Right
            )
        }
    )
    .shuffled()

    private val bridge = Bridge()

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

    suspend fun startTraffic() {
        val startTime = Clock.System.now().toEpochMilliseconds()
        cars
            .map { car ->
                coroutineScope.launch { bridge.passCar(car) }
            }
            .plus(
                additionalCars.map { (delay, car) ->
                    coroutineScope.launch {
                        delay(delay)
                        bridge.passCar(car)
                    }
                }
            )
            .joinAll()
        val endTime = Clock.System.now().toEpochMilliseconds()
        log("riding took ${endTime - startTime} ms")
    }
}
