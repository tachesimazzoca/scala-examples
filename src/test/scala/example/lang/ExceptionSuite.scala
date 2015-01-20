package example.lang

import java.io.{IOException, ByteArrayInputStream, Closeable}

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import scala.util.control.Exception

@RunWith(classOf[JUnitRunner])
class ExceptionSuite extends FunSuite {
  test("Exception.Catch#opt") {
    val no = Exception.allCatch[String].opt {
      throw new IllegalArgumentException("This should be converted to None")
    }
    assert(None === no)

    val yes = Exception.allCatch[String].opt {
      "OK"
    }
    assert(Some("OK") === yes)
  }

  test("Exception.Catch#either") {
    val msg = "This should be converted to Left(Exception)"

    val no = Exception.allCatch[Integer].either {
      throw new IllegalArgumentException(msg)
    }
    assert(no.isLeft)
    assert(no.left.get.isInstanceOf[IllegalArgumentException])
    assert(no.left.get.getMessage() === msg)

    val yes = Exception.allCatch[Integer].either {
      1234
    }
    assert(yes.isRight)
    assert(yes.right.get.isInstanceOf[Integer])
  }

  test("Exception.Catch#withApply") {
    val errorToZero = (t: Throwable) => 0

    val a = Exception.allCatch.withApply(errorToZero).apply {
      123
    }
    assert(a === 123)

    val b = Exception.allCatch.withApply(errorToZero).apply {
      123 / 0
    }
    assert(b === 0)
  }

  test("Exception#catching") {
    val catcher = Exception.catching(
      classOf[IllegalArgumentException],
      classOf[NoSuchElementException]).withApply(_ => "")

    assert("OK" === catcher.apply {
      "OK"
    })

    assert("" === catcher.apply {
      throw new IllegalArgumentException()
    })

    assert("" === catcher.apply {
      throw new NoSuchElementException()
    })

    intercept[Error] {
      catcher.apply {
        throw new Error("This exception should be thrown")
      }
    }
  }

  test("Exception.Catch#andFinally") {
    val expected = "DEAFBEEF"
    val input = new ByteArrayInputStream(expected.getBytes())

    val errorToEmpty = (e: Throwable) => {
      println(e)
      ""
    }
    val closeQuietly = () => {
      println("Closing quietly ...")
      try {
        input.close()
      } catch {
        case e: Throwable =>
      }
    }
    val catcher = Exception.allCatch[String]
      .withApply(errorToEmpty)
      .andFinally(closeQuietly())

    assert(expected === catcher.apply {
      val n = expected.length
      val buf = new Array[Byte](n)
      input.read(buf, 0, n)
      new String(buf)
    })

    assert("" === catcher.apply {
      throw new IOException()
      expected
    })
  }
}
