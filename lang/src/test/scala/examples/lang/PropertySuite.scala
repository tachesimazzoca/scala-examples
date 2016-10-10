package examples.lang

import org.scalatest.FunSuite

class PropertySuite extends FunSuite {

  class Time {
    private[this] var h: Int = _
    private[this] var m: Int = _

    def hour: Int = h

    def hour_=(x: Int) {
      require(x >= 0 && x < 24)
      h = x
    }

    def minute: Int = m

    def minute_=(x: Int) {
      require(x >= 0 && x < 60)
      m = x
    }
  }

  test("Initialized values") {
    val t = new Time
    assert(t.hour === 0)
    assert(t.minute === 0)
  }

  test("Time.hour") {
    val t = new Time
    for (n <- -1 to 24) {
      if (n < 0 || n > 23) {
        intercept[IllegalArgumentException] {
          t.hour = n
        }
      } else {
        t.hour = n
        assert(t.hour === n)
      }
    }
  }

  test("Time.minute") {
    val t = new Time
    for (n <- -1 to 60) {
      if (n < 0 || n > 59) {
        intercept[IllegalArgumentException] {
          t.minute = n
        }
      } else {
        t.minute = n
        assert(t.minute === n)
      }
    }
  }
}
