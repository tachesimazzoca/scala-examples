package examples.snippet

trait Queue[+T] {
  def head: T

  def tail: Queue[T]

  def enqueue[U >: T](x: U): Queue[U]

  def toList: List[T]
}

object Queue {

  def apply[T](xs: T*): Queue[T] = new QueueImpl(xs.toList, Nil)

  private class QueueImpl[T](
    private val ascending: List[T],
    private val descending: List[T]) extends Queue[T] {
    def ascendingQueue: QueueImpl[T] = {
      if (ascending.isEmpty)
        new QueueImpl(descending.reverse, Nil)
      else
        this
    }

    override def head: T = ascendingQueue.ascending.head

    override def tail: Queue[T] = {
      val q = ascendingQueue
      new QueueImpl(q.ascending.tail, q.descending)
    }

    override def enqueue[U >: T](x: U): Queue[U] =
      new QueueImpl[U](ascending, x :: descending)

    override def toList: List[T] = ascending ::: descending.reverse
  }

}
