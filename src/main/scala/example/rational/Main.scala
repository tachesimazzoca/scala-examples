package example.rational

object Main extends App {
  try {
    val dividedZero = new Rational(1, 0)
  } catch {
    case e: Throwable =>
      println(e.getMessage)
  }

  println(new Rational(5))

  val half = new Rational(1, 2)
  println(half)

  val quarter = new Rational(2, 8)
  // 1/4 ... divided by the greatest common devisor
  println(quarter)

  println(half + half)
  println(quarter + half)
}

class Rational(n: Int, d: Int) {
  require(d != 0)

  private val divisor = gcd(n.abs, d.abs)
  private val numer = n / divisor
  private val denom = d / divisor

  def this(n: Int) = this(n, 1)

  def + (ratio: Rational): Rational =
    new Rational(
      numer * ratio.denom + ratio.numer * denom,
      denom * ratio.denom
    )

  override def toString: String = numer + "/" + denom

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}
