import kotlinx.coroutines.*
import org.intership.SCMPQueue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// As far as Lincheck is not supported in Kotlin/Native, we do manual concurrency testing
class ManualConcurrentTest {
    private lateinit var queue: SCMPQueue<Int>

    @BeforeTest
    fun setUp() {
        queue = SCMPQueue()
    }

    @Test
    fun testBasicConcurrency() = runBlocking {
        // Simulate concurrent enqueue
        val jobList = MutableList(10) { id ->
            launch(Dispatchers.Default) {
                repeat(100) {
                    queue.add(id * 100 + it)
                }
            }
        }

        val dequeuedItems = mutableListOf<Int>()
        val readJob = launch(Dispatchers.Default) {
            repeat(1000) {
                val item = queue.poll()
                if (item != null) {
                    dequeuedItems.add(item)
                }
            }
        }

        jobList.add(readJob)
        jobList.forEach { it.join() }

        assertTrue(dequeuedItems.size <= 1000)

        while (queue.isNotEmpty()) {
            dequeuedItems.add(queue.remove())
        }

        assertEquals(1000, dequeuedItems.size)
    }

}