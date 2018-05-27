package examples.fp.data

import examples.fp.Functor

final case class Nested[F[_], G[_], A](value: F[G[A]])

object Nested extends NestedInstances

private[data] sealed abstract class NestedInstances {
  implicit def dataFunctorForNested[F[_] : Functor, G[_] : Functor]: Functor[Nested[F, G, ?]] =
    new NestedFunctor[F, G] {
      val FG: Functor[Lambda[A => F[G[A]]]] = Functor[F].compose[G]
    }
}

private[data] trait NestedFunctor[F[_], G[_]] extends Functor[Nested[F, G, ?]] {
  def FG: Functor[Lambda[A => F[G[A]]]]

  override def map[A, B](fga: Nested[F, G, A])(f: A => B): Nested[F, G, B] =
    Nested(FG.map(fga.value)(f))
}
