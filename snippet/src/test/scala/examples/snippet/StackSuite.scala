package examples.snippet

import org.scalatest.FunSuite

class StackSuite extends FunSuite {
  test("empty") {
    val xs = Stack[Any]()
    intercept[NoSuchElementException] {
      xs.top
    }

    intercept[NoSuchElementException] {
      xs.pop
    }
  }

  test("apply(x: T)") {
    val xs = Stack(1)
    assert(xs.top === 1)
  }

  test("push") {
    val xs = Stack[Int]()
    assert(xs.push(1).push(2).push(3).pop.top === 2)
  }

  test("variance") {
    val stringStack = Stack[String]()
    assert(stringStack.push("abc").top.startsWith("a"))

    // Stack[Any]
    val anyStack = stringStack.push(1).push("def")
    //anyStack.top.startsWith("a") // not compile
  }
}
