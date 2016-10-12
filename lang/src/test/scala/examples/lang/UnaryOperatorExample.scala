package examples.lang

import org.scalatest.FunSuite

class UnaryOperatorExample extends FunSuite {

  class PrefixOpString(s: String) {
    def unary_+ = s.toUpperCase

    def unary_- = s.toLowerCase

    def unary_! = s.reverse

    def unary_~ = (s.toCharArray map { c =>
      if (c.isUpper) c.toLower
      else if (c.isLower) c.toUpper
      else c
    }).mkString("")
  }

  test("unary_* method") {
    assert("FOO" === +(new PrefixOpString("Foo")))
    assert("bar" === -(new PrefixOpString("Bar")))
    assert("zaB" === !(new PrefixOpString("Baz")))
    assert("qUX" === ~(new PrefixOpString("Qux")))
  }

  case class +[+A, +B](_1: A, _2: B)

  case class -[+A, +B](_1: A, _2: B)

  case class ![+A, +B](_1: A, _2: B)

  case class ~[+A, +B](_1: A, _2: B)

  test("case classes using prefix operators: + - ! ~") {
    val plus: Int + Int = new +(1, 2)
    assert(plus.## == plus.hashCode)
    assert(plus == new +(1, 2))
    assert((1, 2) === (plus match {
      case a + b => (a, b)
    }))

    val minus: String - Int = new -("foo", 2)
    assert(minus.## == minus.hashCode)
    assert(minus == new -("foo", 2))
    assert(("foo", 2) === (minus match {
      case a - b => (a, b)
    }))

    val exclamation: Long ! Int = new !(12L, 3)
    assert(exclamation.## == exclamation.hashCode)
    assert(exclamation == new !(12L, 3))
    assert((12L, 3) === (exclamation match {
      case a ! b => (a, b)
    }))

    val tilde: Option[Int] ~ String = new ~(Some(1), "bar")
    assert(tilde.## == tilde.hashCode)
    assert(tilde == new ~(Some(1), "bar"))
    assert((Some(1), "bar") === (tilde match {
      case a ~ b => (a, b)
    }))
  }

  case class &[+A, +B](_1: A, _2: B)

  case class ^[+A, +B](_1: A, _2: B)

  case class |[+A, +B](_1: A, _2: B)

  case class %[+A, +B](_1: A, _2: B)

  test("case classed using unary operators: & ^ | %") {
    val pipe: Int | Int = |(1, 2)
    // illegal variable in pattern alternative
    //assert((1, 2) === (pipe match {
    //  case a | b => (a, b)
    //}))

    val and: Int & Int = &(1, 2)
    assert((1, 2) === (and match {
      case a & b => (a, b)
    }))

    val xor: Int ^ Int = ^(1, 2)
    assert((1, 2) === (xor match {
      case a ^ b => (a, b)
    }))

    val percent: Int % Int = %(1, 2)
    assert((1, 2) === (percent match {
      case a % b => (a, b)
    }))
  }

  test("& as the bitwise AND operator") {
    assert(0xEE === (0xFFEE & 0xFF))
  }

  test("| as the bitwise OR operator") {
    assert(0xFF === (0x77 | 0x88))
  }

  test("^ as the bitwise XOR operator") {
    assert(0x88 === (0x77 ^ 0xFF))
  }

  test("~ as the bitwise NOT operator") {
    assert((0xEE & 0xFF) === (~0x11 & 0xFF))
    assert((0x77 & 0xFF) === (~0x88 & 0xFF))
    assert(0x8000 === (~0x7FFF & 0xFFFF))
  }

}
