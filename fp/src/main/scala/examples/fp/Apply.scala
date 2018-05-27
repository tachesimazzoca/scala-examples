package examples.fp

trait Apply[F[_]] extends Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
}

object Apply {
  def apply[F[_]](implicit ev: Apply[F]): Apply[F] = ev
}
