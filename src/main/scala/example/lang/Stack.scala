package example.lang

trait Stack[+A] {
  def push[B >: A](elem: B): Stack[B]

  def top: A

  def pop: Stack[A]
}

object Stack {
  def apply[T](): Stack[T] = new StackImpl[T]

  def apply[T](x: T): Stack[T] = new StackImpl[T].push(x)

  private class StackImpl[A] extends Stack[A] {
    def push[B >: A](elem: B): Stack[B] = new StackImpl[B] {
      override def top: B = elem

      override def pop: Stack[B] = StackImpl.this
    }

    def top: A = throw new NoSuchElementException

    def pop: Stack[A] = throw new NoSuchElementException
  }

}
