package examples.fp

trait Functor[F[_]] { self =>

  def map[A, B](fa: F[A])(f: A => B): F[B]

  def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)

  def compose[G[_] : Functor]: Functor[Lambda[A => F[G[A]]]] = new ComposedFunctor[F, G] {
    val F = self
    val G = Functor[G]
  }
}

object Functor {
  def apply[F[_]](implicit ev: Functor[F]): Functor[F] = ev
}

private[fp] trait ComposedFunctor[F[_], G[_]] extends Functor[Lambda[A => F[G[A]]]] {

  def F: Functor[F]
  def G: Functor[G]

  override def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] = F.map(fga)(ga => G.map(ga)(f))
}
