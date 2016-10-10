package examples.snippet

import org.scalatest.FunSuite

class RationalSuite extends FunSuite {
  test("toString") {
    assert("1/2" === new Rational(1, 2).toString)
    assert("1/2" === new Rational(2, 4).toString)
    assert("1/2" === new Rational(3, 6).toString)
    assert("2/7" === new Rational(2, 7).toString)
    assert("1/3" === new Rational(3, 9).toString)
    assert("4/9" === new Rational(4, 9).toString)
  }

  test("+") {
    assert("3/2" === (new Rational(1, 2) + new Rational(2, 2)).toString)
    assert("1/1" === (new Rational(1, 2) + new Rational(1, 2)).toString)
  }
}
