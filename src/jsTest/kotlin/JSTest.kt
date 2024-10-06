import org.intership.SCMPQueue
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

// Just a simple test to check that JS tests are running
class JSTest {
    private lateinit var queue: SCMPQueue<Int>

    @BeforeTest
    fun setUp() {
        queue = SCMPQueue()
    }

    @Test
    fun testBasic() {
        queue.add(1)
        assertEquals(1, queue.poll())
    }
}