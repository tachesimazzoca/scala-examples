package example.fp

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class RecursionSuite extends FunSuite {
  import example.fp.Recursion._
  
  test("abs") {
    assert(abs(123) === 123)
    assert(abs(-123) === 123)
  }

  test("factorial") {
    val xs = Array(
      (0, 1),
      (1, 1),
      (2, 2),
      (3, 6),
      (4, 24),
      (5, 120),
      (10, 3628800))
    xs foreach { x =>
      assert(factorial(x._1) === x._2)
    }
  }

  test("fibonacci") {
    val xs = Array(
      (0, 0),
      (1, 1),
      (2, 2),
      (3, 3),
      (4, 5),
      (5, 8),
      (6, 13))
    xs foreach { x =>
      assert(fibonacci(x._1) === x._2)
    }
  }

  test("sqrt") {
    val xs = Array(
      (2d, 1.41421356d),
      (3d, 1.7320508d),
      (4d, 2d),
      (5d, 2.23620679d),
      (6d, 2.44948974d),
      (7d, 2.6457513d),
      (8d, 2.828427d),
      (9d, 3d),
      (10d, 3.1622776d))
    xs foreach { x =>
      assert(abs(sqrt(x._1) - x._2) < 0.01)
    }
  }
}
