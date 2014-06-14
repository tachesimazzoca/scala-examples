package example.lang

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CollectionSuite extends FunSuite {

  test("equals") {
    assert(Seq(1, 2, 3).equals(Seq(1, 2, 3)))
    assert(Set(1, 2, 3).equals(Set(3, 2, 1)))
    assert(Map('foo -> "Foo", 'bar -> "Bar").equals(
      Map('bar -> "Bar", 'foo -> "Foo")))
  }

  test("foreach") {
    var n = 0
    Seq(1, 2, 3, 4) foreach { x =>
      n += 1
      assert(x === n)
    }
    assert(n === 4)
  }

  test("map") {
    assert(Seq(2, 3, 4) === Seq(1, 2, 3).map(_ + 1))
    assert(Set("fo", "ba") === Set("foo", "bar", "baz").map(_.substring(0, 2)))
  }

  test("++") {
    assert(Seq(1, 2, 1, 3) === Seq(1, 2) ++ Seq(1, 3))
    assert(Set(1, 2, 3) === Set(1, 2) ++ Set(1, 3))
    assert(Map('foo -> "Foo2", 'bar -> "Bar") ===
      Map('foo -> "Foo", 'bar -> "Bar") ++ Map('foo -> "Foo2"))
  }

  test("exists") {
    assert(Seq(1, 2, 3).exists(_ == 2))
    assert(Seq(1, 2, 3).exists(_ > 3) === false)

    val odd: Int => Boolean = _ % 2 == 1
    val even: Int => Boolean = _ % 2 == 0
    assert(Seq(1, 2, 3).exists(odd))
    assert(Seq(1, 2, 3).exists(even))
  }

  test("forall") {
    assert(Seq(1, 2, 3).forall(_ > 0))
    assert(Seq("foo", "bar").forall(!_.isEmpty))

    val odd: Int => Boolean = _ % 2 == 1
    val even: Int => Boolean = _ % 2 == 0
    assert(Seq(1, 2, 3).forall(odd) === false)
    assert(Seq(1, 2, 3).forall(even) === false)
  }

  test("Seq#fill") {
    assert(Seq("a", "a", "a") === Seq.fill(3)("a"))
  }

  test("Seq#range") {
    assert(Seq(0, 1, 2) === Seq.range(0, 3))
  }
}
