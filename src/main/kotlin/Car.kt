import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.withLock

data class Car(
    val id: Int,
    val direction: Direction
)
