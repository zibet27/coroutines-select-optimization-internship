import kotlin.test.*
import org.intership.SCMPQueue

class BasicSequentialTest {
    private lateinit var queue: SCMPQueue<Int>

    @BeforeTest
    fun setUp() {
        queue = SCMPQueue()
    }

    @Test
    fun testBasic() {
        queue.add(1)
        assertEquals(queue.poll(), 1)
        assertEquals(queue.peek(), null)
        queue.add(2)
        assertEquals(queue.poll(), 2)
    }

    @Test
    fun testAddAndElement() {
        queue.add(1)
        queue.add(2)
        assertEquals(1, queue.poll()) // Check that the first element is 1
    }

    @Test
    fun testAddAndRemove() {
        queue.add(1)
        queue.add(2)
        assertEquals(1, queue.remove()) // Removes and returns 1
        assertEquals(2, queue.peek()) // Now the first element should be 2
    }

    @Test
    fun testOfferAndPoll() {
        assertTrue(queue.offer(10)) // Check that offer returns true when adding an element
        assertEquals(10, queue.poll()) // Check that poll removes and returns the element
        assertEquals(0, queue.size) // Check that the size is now 0
        assertNull(queue.poll()) // Check that poll returns null when the queue is empty
    }

    @Test
    fun testPeek() {
        assertNull(queue.poll()) // Ensure peek returns null for an empty queue
        queue.add(5)
        assertEquals(5, queue.peek()) // Peek returns the first element but does not remove it
        assertEquals(5, queue.poll()) // Ensure it's still there
    }

    @Test
    fun testRemoveThrowsExceptionOnEmptyQueue() {
        assertFailsWith<NoSuchElementException> {
            queue.remove() // Should throw an exception when the queue is empty
        }
    }

    @Test
    fun testElementThrowsExceptionOnEmptyQueue() {
        assertFailsWith<NoSuchElementException> {
            queue.element() // Should also throw an exception when empty
        }
    }

    @Test
    fun testAddRemoveManyItems() {
        for (i in 0 until 1000) {
            queue.add(i)
        }
        for (i in 0 until 1000) {
            assertEquals(i, queue.remove())
        }
    }

    @Test
    fun testAddRemoveManyItemsRandom() {
        for (i in 0 until 1000) {
            val removeCnt = (1..1000).random()
            val addCnt = (removeCnt..removeCnt * 2).random()

            for (j in 0 until addCnt) {
                queue.add(j)
            }
            assertEquals(addCnt, queue.size)
            for (j in 0 until removeCnt) {
                assertEquals(j, queue.remove())
            }
            assertEquals(queue.size, addCnt - removeCnt)
            queue = SCMPQueue()
        }
    }

    @Test
    fun testIterator() {
        queue.add(1)
        queue.add(2)
        queue.add(3)
        queue.add(4)
        queue.add(5)
        val iterator = queue.iterator()
        assertEquals(1, iterator.next())
        assertEquals(2, iterator.next())
        assertEquals(3, iterator.next())
        assertEquals(4, iterator.next())
        assertEquals(5, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun testContains() {
        queue.add(1)
        queue.add(2)
        queue.add(3)
        assertTrue(1 in queue)
        assertTrue(2 in queue)
        assertTrue(3 in queue)
        assertFalse(4 in queue)
    }

    @Test
    fun testIsEmpty() {
        assertTrue(queue.isEmpty())
        queue.add(1)
        assertFalse(queue.isEmpty())
        queue.poll()
        assertTrue(queue.isEmpty())
    }

    @Test
    fun testContainsAll() {
        queue.add(1)
        queue.add(2)
        queue.add(3)
        assertTrue(queue.containsAll(listOf(1, 2, 3)))
        assertFalse(queue.containsAll(listOf(1, 2, 3, 4)))
    }
}