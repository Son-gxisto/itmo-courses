package dijkstra

import kotlinx.atomicfu.locks.ReentrantLock
import java.util.*
import java.util.concurrent.Phaser
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.Lock
import kotlin.Comparator
import kotlin.concurrent.thread

private val NODE_DISTANCE_COMPARATOR = Comparator<Node> { o1, o2 -> Integer.compare(o1!!.distance, o2!!.distance) }

// Returns `Integer.MAX_VALUE` if a path has not been found.
fun shortestPathParallel(start: Node) {
    val workers = Runtime.getRuntime().availableProcessors()
    // The distance to the start node is `0`
    start.distance = 0
    // Create a priority (by distance) queue and add the start node into it
    val q = MultiQueue(workers, NODE_DISTANCE_COMPARATOR)
    q.add(start)
    val activeNodes = AtomicInteger(1)
    // Run worker threads and wait until the total work is done
    val onFinish = Phaser(workers + 1) // `arrive()` should be invoked at the end by each worker
    repeat(workers) {
        thread {
            while (activeNodes.get() > 0) {
                val cur: Node = q.poll() ?: continue
                val d = cur.distance
                for (e in cur.outgoingEdges) {
                    val to = e.to.distance
                    val newd = d + e.weight
                    if (to > newd) {
                        e.to.casDistance(to, newd)
                        q.add(e.to)
                        activeNodes.incrementAndGet()
                    }
                }
                activeNodes.decrementAndGet()
            }
            onFinish.arrive()
        }
    }
    onFinish.arriveAndAwaitAdvance()
}

class LockQueue<T>(comparator: Comparator<T>) {
    val queue = PriorityQueue(comparator)
    val lock: Lock = ReentrantLock()
}

class MultiQueue<T>(n: Int, private val comparator: Comparator<T>) {
    private val countQ: Int = n * 2;
    private val queues = Array(countQ) { LockQueue(comparator) }
    private val random: Random = Random()
    fun add(el: T) {
        while (true) {
            val num: Int = random.nextInt(countQ)
            val curQ = queues[num]
            if (curQ.lock.tryLock()) {
                try {
                    curQ.queue.add(el)
                    return
                } finally {
                    curQ.lock.unlock()
                }
            }
        }
    }

    fun poll(): T? {
        while (true) {
            val q1 = queues[random.nextInt(countQ)]
            val q2 = queues[random.nextInt(countQ)]
            if (q1.lock.tryLock()) {
                try {
                    if (q2.lock.tryLock()) {
                        try {
                            val e1 = q1.queue.peek()
                            val e2 = q2.queue.peek()
                            if (e1 == null && e2 != null) {
                                return q2.queue.poll()
                            }
                            if (e1 != null && e2 == null) {
                                return q1.queue.poll()
                            }
                            if (e1 != null && e2 != null) {
                                return if (comparator.compare(e1, e2) < 0) {
                                    q1.queue.poll()
                                } else {
                                    q2.queue.poll()
                                }
                            }
                            return null
                        } finally {
                            q2.lock.unlock()
                        }
                    }
                } finally {
                    q1.lock.unlock()
                }
            }
        }
    }
}