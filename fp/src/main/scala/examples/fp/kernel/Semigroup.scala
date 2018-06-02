package examples.fp.kernel

trait Semigroup[A] {
  def combine(x: A, y: A): A
}

object Semigroup {
  def apply[A: Semigroup](implicit ev: Semigroup[A]): Semigroup[A] = ev

  def maybeCombine[A: Semigroup](x: A, oy: Option[A]): A =
    oy.map(Semigroup[A].combine(x, _)).getOrElse(x)

  def maybeCombine[A: Semigroup](ox: Option[A], y: A): A =
    ox.map(Semigroup[A].combine(_, y)).getOrElse(y)
}
