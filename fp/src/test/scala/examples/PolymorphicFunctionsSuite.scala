package examples.fp

import org.scalatest.FunSuite

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

  test("const") {
    // identity: A => A
    assert(identity(1) === 1)
    assert(identity("foo") === "foo")

    // const: A => B => A
    def f[B]: (=> B) => Int = const(1)
    assert(f("foo") === 1)
    assert(f(2) === 1)
    assert(f(println("Keep calm and do nothing.")) === 1)
    assert(f(println) === 1)

    // const(identity): B => A => A
    def g[B, A]: (=> B) => A => A = const(identity)
    assert(g("foo")(1) === 1)
    assert(g(2)(1) === 1)
    assert(g(println("Keep calm and do nothing."))("foo") === "foo")
    assert(g(println)(123) === 123)

    val xs = Seq("foo", "bar", "baz")
    // the length of xs
    assert(xs.foldRight(0)((_, a) => a + 1) === xs.size)
    def inc: Int => Int = _ + 1
    assert(xs.foldRight(0)(uncurry(const(inc)(_: Any))) === xs.size)
    // the last item of xs
    assert(xs.reduceRight((_, a) => a) === "baz")
    assert(xs.reduceRight(uncurry(const(identity(_: String))(_: Any))) === "baz")
  }
}
