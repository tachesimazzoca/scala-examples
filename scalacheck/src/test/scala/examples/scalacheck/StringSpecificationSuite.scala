package examples.scalacheck

import org.scalacheck.Prop._
import org.scalacheck.Properties
import org.scalatest.FunSuite

class StringSpecificationSuite extends FunSuite {
  test("StringSpecification") {
    StringSpecification.check()
  }

  object StringSpecification extends Properties("String") {
    property("startsWith") = forAll { (a: String, b: String) =>
      (a + b).startsWith(a)
    }

    property("concatenate") = forAll { (a: String, b: String) =>
      if (a.isEmpty && b.isEmpty) (a + b).length == 0
      else if (a.isEmpty) (a + b).length == b.length
      else if (b.isEmpty) (a + b).length == a.length
      else (a + b).length > a.length && (a + b).length > b.length
    }

    property("substring") = forAll { (a: String, b: String, c: String) =>
      (a + b + c).substring(a.length, a.length + b.length) == b
    }
  }

}
