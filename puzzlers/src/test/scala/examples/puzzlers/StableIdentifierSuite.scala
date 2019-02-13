package examples.puzzlers

import org.scalatest.FunSuite

class StableIdentifierSuite extends FunSuite {

  test("Variables starting with an uppercase letter are stable identifiers.") {

    val (foo, bar) = (1, 2)
    assert(1 === foo)
    assert(2 === bar)

    //val (Foo, Bar) = (1, 2) // won't compile.

    val Bar = "Baz"
    assert(("Baz" match {
      case Bar => true
      case _ => false
    }) === true)
  }

  test("Suspicious shadowing by a Variable Pattern.") {

    val foo = "foo?"

    // Suspicious shadowing by a Variable Pattern
    assert(("foo" match {
      case foo => true
      case _ => false
    }) === true)

    // Use backtick to perform an equality test
    assert(("foo?" match {
      case `foo` => true
      case _ => false
    }) === true)
  }
}
