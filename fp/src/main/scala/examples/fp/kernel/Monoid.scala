package examples.fp.kernel

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}

object Monoid {
  def apply[A](implicit ev: Monoid[A]): Monoid[A] = ev
}
