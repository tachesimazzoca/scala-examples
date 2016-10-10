package examples.snippet

import org.scalatest.FunSuite

class QueueSuite extends FunSuite {
  test("head") {
    val q: Queue[Int] = Queue(1, 2, 3)
    assert(1 === q.head)
  }

  test("tail") {
    val q: Queue[Int] = Queue(1, 2, 3)
    assert(q.tail.toList === List(2, 3))
  }

  test("enqueue") {
    val q: Queue[Int] = Queue(1, 2, 3)
    assert(q.enqueue(4).toList == List(1, 2, 3, 4))
  }

  test("enqueue[U >: T] using a lower bound") {
    val q: Queue[Int] = Queue(1, 2, 3)
    assert(q.enqueue("a").toList == List(1, 2, 3, "a"))
  }
}
