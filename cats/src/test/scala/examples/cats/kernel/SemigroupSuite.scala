package examples.cats.kernel

import org.scalatest.{FunSuite, Matchers}
import cats.implicits._

class SemigroupSuite extends FunSuite with Matchers {

  test("Int") {
    ((1 |+| 2) |+| 3) should ===(1 |+| (2 |+| 3))
    (1 |+| 2 |+| 3) should ===(6)
  }

  test("String") {
    (("a" |+| "b") |+| "c") should ===("a" |+| ("b" |+| "c"))
    ("a" |+| "bc" |+| "") should ===("abc")
  }

  test("List") {
    ((List(1, 2) |+| List(3, 4, 5)) |+| List(6)) should ===(List(1, 2) |+| (List(3, 4, 5) |+| List(6)))
    (List(1, 2) |+| List(3, 4, 5) |+| List(6)) should ===(List(1, 2, 3, 4, 5, 6))
  }

  test("Tuple") {
    ("Foo", 1, List(1, 2)) |+| ("Bar", 2, List(3)) should ===("FooBar", 3, List(1, 2, 3))
  }
}
