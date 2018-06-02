package examples.fp.kernel

import org.scalatest.FunSuite

class MonoidSuite extends FunSuite {

  implicit val intAdditionMonoid = new Monoid[Int] {
    override def empty: Int = 0
    override def combine(x: Int, y: Int): Int = x + y
  }

  implicit val stringAdditionMonoid = new Monoid[String] {
    override def empty: String = ""
    override def combine(x: String, y: String): String = x + y
  }

  implicit def seqAdditionMonoid[A]: Monoid[Seq[A]] = new Monoid[Seq[A]] {
    override def empty: Seq[A] = Seq.empty[A]
    override def combine(x: Seq[A], y: Seq[A]): Seq[A] = x ++ y
  }

  case class Pair[T1, T2](first: T1, second: T2)

  implicit def pairAdditionMonoid[A: Monoid, B: Monoid]: Monoid[Pair[A, B]] =
    new Monoid[Pair[A, B]] {
      override def empty: Pair[A, B] = Pair(Monoid[A].empty, Monoid[B].empty)

      override def combine(x: Pair[A, B], y: Pair[A, B]): Pair[A, B] =
        Pair(Monoid[A].combine(x.first, y.first), Monoid[B].combine(x.second, y.second))
    }

  def combineAll[A: Monoid](xs: Seq[A]): A =
    xs.foldRight(Monoid[A].empty)(Monoid[A].combine(_, _))

  test("intAdditionMonoid") {
    assert(10 === combineAll(Seq(1, 2, 3, 4)))
  }

  test("stringAdditionMonoid") {
    assert("foobarbaz" === combineAll(Seq("foo", "bar", "baz")))
  }

  test("seqAdditionMonoid") {
    assert(Seq("foo", "bar", "baz", "qux") === combineAll(Seq(Seq("foo", "bar"), Seq("baz", "qux"))))
  }

  test("pairAdditionMonoid") {
    assert(Pair("foobarbaz", 6) === combineAll(Seq(Pair("foo", 1), Pair("bar", 2), Pair("baz", 3))))
  }
}
