package example.fp

object PolymorphicFunctions {
  def findFirst[A](xs: Array[A], f: A => Boolean): Int = {
    @annotation.tailrec
    def loop(i: Int): Int = {
      if (i >= xs.length) -1
      else {
        if (f(xs(i))) i
        else loop(i + 1)
      }
    }
    loop(0)
  }

  def isSorted[A](xs: Array[A], gt: (A, A) => Boolean): Boolean = {
    @annotation.tailrec
    def loop(i: Int): Boolean = {
      if (i >= xs.length - 1) true
      else {
        if (gt(xs(i), xs(i + 1))) loop(i + 1)
        else false
      }
    }
    loop(0)
  }

  def partial[A, B, C](a: A, f: (A, B) => C): B => C =
    (b: B) => f(a, b)

  def curry[A, B, C](f: (A, B) => C): A => B => C =
    a => b => f(a, b)

  def uncurry[A, B, C](f: A => B => C): (A, B) => C =
    (a, b) => f(a)(b)

  def compose[A, B, C](f: B => C, g: A => B): A => C =
    a => f(g(a))
}
