package example.quickcheck

import org.junit.runner.RunWith

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop._
import org.scalacheck.Properties

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class QuickCheckSuite extends FunSuite with Checkers {
  test("StringSpecification") {
    check(StringSpecification)
  }

  test("MathSpecification") {
    check(MathSpecification)
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

  object MathSpecification extends Properties("Math") {
    val gen = for {
      n <- arbitrary[Int]
      if (n >= 0)
    } yield n

    property("abs with Gen") = forAll(gen) { (n: Int) =>
      math.abs(n) == n
      math.abs(n * -1) == n
    }
  }
}
