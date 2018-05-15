package examples.fp.kernel

import org.scalatest.FunSuite

class SemigroupSuite extends FunSuite {

  implicit val intAdditionSemigroup = new Semigroup[Int] {
    def combine(x: Int, y: Int): Int = x + y
  }

  implicit val stringAdditionSemigroup = new Semigroup[String] {
    def combine(x: String, y: String): String = x + y
  }

  implicit def listAdditionSemigroup[A] = new Semigroup[List[A]] {
    def combine(x: List[A], y: List[A]): List[A] = x ++ y
  }

  test("intAdditionSemigroup") {
    val x = 1
    val y = 2
    val z = 3

    val a = Semigroup[Int].combine(x, Semigroup[Int].combine(y, z))
    val b = Semigroup[Int].combine(Semigroup[Int].combine(x, y), z)

    assert(a === b)
  }

  test("stringAdditionSemigroup") {
    val x = "foo"
    val y = "bar"
    val z = "baz"

    val a = Semigroup[String].combine(x, Semigroup[String].combine(y, z))
    val b = Semigroup[String].combine(Semigroup[String].combine(x, y), z)

    assert(a === b)
  }

  test("listAdditionSemigroup") {
    val x = List(1)
    val y = List(2, 3)
    val z = List(4, 5, 6)

    val a = Semigroup[List[Int]].combine(x, Semigroup[List[Int]].combine(y, z))
    val b = Semigroup[List[Int]].combine(Semigroup[List[Int]].combine(x, y), z)

    assert(a === b)
  }

  test("mergeMap") {
    import Semigroup.maybeCombine

    def mergeMap[K, V: Semigroup](x: Map[K, V], y: Map[K, V]): Map[K, V] = x.foldLeft(y) {
      case (acc, (k, v)) => acc.updated(k, maybeCombine(v, acc.get(k)))
    }

    assert(mergeMap(Map('foo -> 1, 'bar -> 2), Map('foo -> 3, 'baz -> 1))
      === Map('foo -> 4, 'bar -> 2, 'baz -> 1))

    assert(mergeMap(Map(1 -> List(1, 2), 2 -> List(4)), Map(1 -> List(3), 2 -> List(3, 5)))
      === Map(1 -> List(1, 2, 3), 2 -> List(4, 3, 5)))
  }
}
