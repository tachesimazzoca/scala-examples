package example.quickcheck

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import org.scalatest.prop.Checkers
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

@RunWith(classOf[JUnitRunner])
class QuickCheckSuite extends FunSuite with Checkers {
  test("StringSpecification") {
    check(StringSpecification)
  }
}

object StringSpecification extends Properties("String") {
  property("startsWith") = forAll { (a: String, b: String) =>
    (a+b).startsWith(a)
  }

  property("concatenate") = forAll { (a: String, b: String) =>
    if (a.isEmpty && b.isEmpty) (a + b).length == 0
    else if (a.isEmpty) (a + b).length == b.length
    else if (b.isEmpty) (a + b).length == a.length
    else (a+b).length > a.length && (a+b).length > b.length
  }

  property("substring") = forAll { (a: String, b: String, c: String) =>
    (a+b+c).substring(a.length, a.length+b.length) == b
  }
}
