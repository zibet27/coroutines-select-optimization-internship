package org.intership

import kotlinx.atomicfu.atomic

/**
 * A queue interface extending the Collection interface.
 * Inspired by Java Queue.
 */
interface Queue<E> : Collection<E> {
    /**
     * Retrieves, but does not remove, the head of this queue.
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun element(): E

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    fun peek(): E?

    /**
     * Retrieves and removes the head of this queue.
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun remove(): E

    /**
     * Inserts the specified element into this queue.
     * @param element the element to add
     * @throws IllegalStateException if the element cannot be added
     */
    fun add(element: E)

    /**
     * Inserts the specified element into this queue if possible.
     * @param element the element to add
     * @return true if the element was added, false otherwise
     */
    fun offer(element: E): Boolean

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    fun poll(): E?
}

/**
 * A thread-safe, lock-free single consumer multiple producers queue implementation.
 * @param T the type of elements held in this queue
 */
class SCMPQueue<T : Any> : Queue<T> {

    /**
     * Node class representing an element in the queue.
     */
    private class Node<T>(val element: T?) {
        private val atomicNext = atomic<Node<T>?>(null)

        /**
         * @return the next node
         */
        val next get() = atomicNext.value

        /**
         * Sets the next node if it is currently null.
         * @return true if the next node was set, false otherwise
         */
        fun setNext(node: Node<T>) = atomicNext.compareAndSet(null, node)
    }

    private var head = Node<T?>(null)
    private val tail = atomic(head)
    private val atomicSize = atomic(0)

    /**
     * Retrieves, but does not remove, the head of this queue.
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    override fun element(): T {
        return peek() ?: throw NoSuchElementException()
    }

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    override fun peek(): T? {
        return head.next?.element
    }

    /**
     * Retrieves and removes the head of this queue.
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    override fun remove(): T {
        return poll() ?: throw NoSuchElementException()
    }

    /**
     * Inserts the specified element into this queue.
     * @param element the element to add
     * @throws IllegalStateException if the element cannot be added
     */
    override fun add(element: T) {
        offer(element) || throw IllegalStateException("Failed to add element")
    }

    /**
     * Inserts the specified element into this queue if possible.
     * @param element the element to add
     * @return true if the element was added, false otherwise
     */
    override fun offer(element: T): Boolean {
        val newNode = Node<T?>(element)
        while (true) {
            val curTail = tail.value
            if (curTail.setNext(newNode)) {
                atomicSize.incrementAndGet()
                tail.compareAndSet(curTail, newNode)
                return true
            }
            tail.compareAndSet(curTail, curTail.next ?: Node(null))
        }
    }

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     * @return the head of this queue, or null if this queue is empty
     */
    override fun poll(): T? {
        val headNext = head.next ?: return null
        head = headNext
        atomicSize.decrementAndGet()
        return headNext.element
    }

    /**
     * @return the number of elements in this queue
     */
    override val size: Int
        get() = atomicSize.value

    /**
     * @return true if this queue contains no elements
     */
    override fun isEmpty(): Boolean {
        return size == 0
    }

    /**
     * @param elements collection to be checked for containment in this queue
     * @return true if this queue contains all the elements in the specified collection
     */
    override fun containsAll(elements: Collection<T>): Boolean {
        return elements.all { contains(it) }
    }

    /**
     * @param element element whose presence in this queue is to be tested
     * @return true if this queue contains the specified element
     */
    override fun contains(element: T): Boolean {
        for (e in this) {
            if (e == element) return true
        }
        return false
    }

    /**
     * An iterator over the elements in this queue.
     * @param head the head node of the queue
     */
    private class QueueIterator<T>(head: Node<T?>) : Iterator<T> {
        private var current = head

        /**
         * @return true if the iteration has more elements
         */
        override fun hasNext(): Boolean = current.next != null

        /**
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        override fun next(): T {
            current = current.next ?: throw NoSuchElementException()
            return current.element ?: throw NoSuchElementException()
        }
    }

    /**
     * @return weakly consistent iterator over the elements in this queue
     */
    override fun iterator(): Iterator<T> {
        return QueueIterator(head)
    }
}