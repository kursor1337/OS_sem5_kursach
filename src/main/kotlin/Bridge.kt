import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class Bridge {
    private val mutex = Mutex()
    private val waitingCars = ConcurrentList<Car>()

    suspend fun passCar(car: Car) {
        // добавляем машину в очередь машин
        waitingCars.safeAdd(car)
        while (waitingCars.safeContains(car)) { // проверка на наличие машины в очереди
            delay(50)
            mutex.withLock {
                if (waitingCars.maxOf { it.direction.priority } > car.direction.priority) {
                    return@withLock // если есть машины в более высоким приоритетом - отказываемся ехать по мосту
                }
                waitingCars.safeRemove(car) // убираем машину из очереди
                log("$car is riding on bridge")
                delay(car.durationOfRide.toLong())      // проезд по мосту
                log("$car came out of bridge")
            }
        }
    }
}


