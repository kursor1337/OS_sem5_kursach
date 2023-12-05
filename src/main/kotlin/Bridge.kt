import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.Exception

class Bridge {
    private val mutex = Mutex()
    private val waitingCars = ConcurrentList<Car>()

    suspend fun passCar(car: Car) {
        waitingCars.safeAdd(car)
        while (waitingCars.safeContains(car)) {
            delay(50)
            mutex.withLock {
                if (waitingCars.maxOf { it.direction.priority } > car.direction.priority) return@withLock
                waitingCars.safeRemove(car)
                log("$car is riding on bridge")
                delay(2000)
                log("$car came out of bridge")
            }
        }

    }
}


