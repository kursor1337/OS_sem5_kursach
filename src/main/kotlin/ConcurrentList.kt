import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ConcurrentList<T>(list: MutableList<T> = mutableListOf()) : MutableList<T> by list {
    private val mutex = Mutex()
    private val _dataFlow = MutableStateFlow<List<T>>(list)

    suspend fun safeGet(index: Int): T {
        return mutex.withLock { get(index) }
    }
    
    suspend fun safeSet(index: Int, element: T) {
        mutex.withLock {
            set(index, element)
            _dataFlow.value = this.toList()
        }
    }
    
    suspend fun safeAdd(element: T) {
        mutex.withLock {
            add(element)
            _dataFlow.value = this.toList()
        }
    }
    
    suspend fun safeRemove(element: T) {
        mutex.withLock {
            remove(element)
            _dataFlow.value = this.toList()
        }
    }

    suspend fun safeContains(element: T): Boolean {
        return mutex.withLock { contains(element) }
    }
}