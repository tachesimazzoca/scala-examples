package examples.scalacheck

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop._
import org.scalacheck.Properties
import org.scalatest.FunSuite

class MathSpecificationSuite extends FunSuite {
  test("MathSpecification") {
    MathSpecification.check()
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
