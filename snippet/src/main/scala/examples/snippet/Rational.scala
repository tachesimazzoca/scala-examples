package examples.snippet

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
