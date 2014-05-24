package example.fp

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class PolymorphicFunctionsSuite extends FunSuite {
  import PolymorphicFunctions._

  test("findFirst") {
    val odd: Int => Boolean = _ % 2 == 1
    val even: Int => Boolean = _ % 2 == 0
    assert(findFirst(Array(1, 2, 3), odd) === 0)
    assert(findFirst(Array(0, 2, 3), odd) === 2)
    assert(findFirst(Array(1, 2, 3), even) === 1)
    assert(findFirst(Array(1, 3, 5, 6), even) === 3)

    assert(findFirst(Array("foo", "bar", "baz"),
      (s: String) => s == "bar") === 1)
  }

  test("isSorted") {
    val gtInt: (Int, Int) => Boolean = (_ <= _)
    assert(isSorted(Array(), gtInt))
    assert(isSorted(Array(1, 1, 1), gtInt))
    assert(isSorted(Array(1, 1, 2), gtInt))
    assert(isSorted(Array(1, 2, 3), gtInt))
    assert(!isSorted(Array(1, 2, 1), gtInt))

    val gtString: (String, String) => Boolean = (_ <= _)
    assert(isSorted(Array(), gtString))
    assert(isSorted(Array("abc", "abd"), gtString))
    assert(!isSorted(Array("abc", "abb"), gtString))
  }

  test("partial") {
    val version = partial("v%d.%d.%d",
      (s: String, ver: (Int, Int, Int)) => s.format(ver._1, ver._2, ver._3))
    assert(version((1, 2, 3)) === "v1.2.3")
  }

  test("curry") {
    val formatVersion = curry(
      (s: String, ver: (Int, Int, Int)) => s.format(ver._1, ver._2, ver._3))
    val version = formatVersion("v%d.%d.%d")
    assert(version((1, 2, 3)) === "v1.2.3")
  }

  test("uncurry") {
    val formatVersion = uncurry(
      (s: String) => (ver: (Int, Int, Int)) => s.format(ver._1, ver._2, ver._3))
    assert(formatVersion("v%d.%d.%d", (1, 2, 3)) === "v1.2.3")
    assert(formatVersion("ver%d.%d.%d", (1, 2, 3)) === "ver1.2.3")
  }

  test("compose") {
    val trim: String => String = _.trim
    val decorate: String => String = (s) => {
      if (s.isEmpty) s
      else "[%s]".format(s)
    }
    val label = compose(decorate, trim)
    assert(label("") === "")
    assert(label("   ") === "")
    assert(label("foo") === "[foo]")
    assert(label(" bar ") === "[bar]")
    assert(label("baz\n") === "[baz]")
    assert(label("qu x") === "[qu x]")
  }
}
