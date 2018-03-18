package examples.fp

import scala.language.higherKinds

object TypeClass {

  trait Functor[F[_]] {
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  trait FlatMap[F[_]] extends Functor[F] {
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  }

  implicit class FlatMapOps[F[_], A](fa: F[A])(implicit F: FlatMap[F]) {
    def map[B](f: A => B): F[B] = F.map(fa)(f)

    def flatMap[B](f: A => F[B]): F[B] = F.flatMap(fa)(f)
  }

}
