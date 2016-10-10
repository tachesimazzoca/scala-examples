package examples.fp

object Recursion {
  def abs(x: Double): Double = if (x < 0) -x else x

  def factorial(x: Double): Double = {
    @annotation.tailrec
    def loop(acc: Double, n: Double): Double = {
      if (n > 0) loop(acc * n, n - 1)
      else acc
    }
    loop(1, x)
  }

  def fibonacci(x: Double): Double = {
    @annotation.tailrec
    def loop(x: Double, y: Double, n: Double): Double = {
      if (n > 1) {
        loop(y, x + y, n - 1)
      } else x + y
    }
    if (x > 0) loop(0, 1, x)
    else 0
  }

  /**
   * <code>
   * def sqrt(x: Double) = {
   *   def isGoodEnough(guess: Double) =
   *     abs((guess * guess) - x) / x &lt; 0.001
   *
   *   def improve(guess: Double) =
   *     (guess + (x / guess)) / 2
   *
   *   @annotation.tailrec
   *   def loop(acc: Double): Double = {
   *     if (isGoodEnough(acc)) acc
   *     else loop(improve(acc))
   *   }
   *   loop(1.0)
   * }
   * </code>
   */
  def sqrt(x: Double): Double =
    fixedPoint(averageDamp(x / _))(1.0)

  private def averageDamp(f: Double => Double)(x: Double) =
    (x + f(x)) / 2

  private def isClosedEnough(a: Double, b: Double) =
    abs((a - b) / a) / a < 0.001

  private def fixedPoint(f: Double => Double)(guess: Double): Double = {
    @annotation.tailrec
    def loop(acc: Double): Double = {
      val next = f(acc)
      if (isClosedEnough(acc, next)) acc
      else loop(next)
    }
    loop(guess)
  }
}
