import org.intership.SCMPQueue
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.check
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import kotlin.test.Test

class ConcurrentTest {
    private val queue = SCMPQueue<Int>()

    @Operation
    fun add(e: Int) = queue.add(e)

    @Operation(nonParallelGroup = "consumer")
    fun poll() = queue.poll()

    @Operation(nonParallelGroup = "consumer")
    fun peek() = queue.peek()

    //Run Lincheck in the stress testing mode
    @Test
    fun stressTest() = StressOptions().check(this::class)

    // Run Lincheck in the model checking testing mode
    @Test
    fun modelCheckingTest() = ModelCheckingOptions().check(this::class)
}