package examples.fp

import org.scalatest.FunSuite

class FunctorSuite extends FunSuite {
  implicit val functorForOption = new Functor[Option] {
    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  implicit val functorForList = new Functor[List] {
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  }

  test("functorForOption") {
    val F = Functor[Option]
    val f: Int => Int = _ + 1
    val g: Int => Int = _ * 10

    assert(F.map(Some(1))(identity) === Some(1))
    assert(F.map(F.map(Some(1))(f))(g) === Some(20))
    assert(F.map(Some(1))(f.andThen(g)) === Some(20))
    assert(F.map(F.map(None)(f))(g) === None)
    assert(F.map(None)(f.andThen(g)) === None)
  }

  test("functorForList") {
    val F = Functor[List]
    val f: Int => Int = _ + 1
    val g: Int => Int = _ * 10

    assert(F.map(List(1, 2, 3))(identity) === List(1, 2, 3))
    assert(F.map(F.map(List(1, 2, 3))(f))(g) === List(20, 30, 40))
    assert(F.map(List(1, 2, 3))(f.andThen(g)) === List(20, 30, 40))
    assert(F.map(F.map(List.empty[Int])(f))(g) === List.empty[Int])
    assert(F.map(List.empty[Int])(f.andThen(g)) === List.empty[Int])
  }

  test("lift") {
    val F = Functor[Option]
    val f = F.lift((_ * 2): Int => Int)

    assert(f(Some(1)) === Some(2))
    assert(f(None) === None)
  }

  test("compose") {
    val F = Functor[List]
    val G = Functor[Option]
    val FG = F.compose(G)

    assert(FG.map(List(Some(1), None, Some(3)))(_ + 1) === List(Some(2), None, Some(4)))
  }
}
