package examples.scalacheck

import java.util.Calendar

import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalacheck.Prop._
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

class GeneratorSuite extends FunSuite with Checkers {
  test("Gen.choose") {
    val aGen = Gen.choose(0, 49)
    val bGen = Gen.choose(50, 99)
    check(forAll(aGen, bGen) { (a: Int, b: Int) =>
      math.min(a, b) == a
      math.max(a, b) == b
    })
  }

  test("arbitrary[Date]") {
    check(forAll(arbitrary[java.util.Date]) { (date: java.util.Date) =>
      val cal = Calendar.getInstance
      cal.setTime(date)
      val ymd1 = "%d-%02d-%02d".format(
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DATE))
      val ymd2 = "%tY-%<tm-%<td".format(date)
      ymd1 == ymd2
    })
  }
}
