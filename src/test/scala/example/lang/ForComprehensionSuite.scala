package example.lang

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.{ Try, Success, Failure }

@RunWith(classOf[JUnitRunner])
class ForComprehensionSuite extends FunSuite {
  case class User(email: String, name: String)

  def optUser(params: Map[String, String]): Option[User] =
    for {
      email <- params.get("email") if !email.isEmpty
      name <- params.get("name") if !name.isEmpty
    } yield User(email, name)

  def optUser2(params: Map[String, String]): Option[User] =
    params.get("email").withFilter(!_.isEmpty) flatMap { email =>
      params.get("name").withFilter(!_.isEmpty) map { name =>
        User(email, name)
      }
    }

  test("optUser: valid params") {
    val params =
      Map(
        "email" -> "foo@example.net",
        "name" -> "Foo")
    val expected = Some(User("foo@example.net", "Foo"))
    assert(optUser(params) === expected)
    assert(optUser2(params) === expected)
  }

  test("optUser: empty map") {
    assert(optUser(Map.empty[String, String]) === None)
    assert(optUser2(Map.empty[String, String]) === None)
  }

  test("optUser: invalid keys") {
    assert(optUser(Map("name" -> "Foo")) === None)
    assert(optUser2(Map("name" -> "Foo")) === None)
  }

  test("optUser: email is empty") {
    assert(optUser(Map("email" -> "", "name" -> "Foo")) === None)
    assert(optUser2(Map("email" -> "", "name" -> "Bar")) === None)
  }

  test("optUser: name is empty") {
    assert(optUser(Map("email" -> "foo@example.net", "name" -> "")) === None)
    assert(optUser2(Map("email" -> "bar@example.net", "name" -> "")) === None)
  }

  object BankAccount {
    def withdraw(balance: Int, amount: Int): Try[Int] = {
      val x = balance - amount
      if (x >= 0) Success(x)
      else Failure(new Error("balance is less than " + amount))
    }
  }

  test("BankAccount") {
    def f(balance: String, a: String) = for {
      x <- Try(balance.toInt)
      y <- Try(a.toInt)
      b <- BankAccount.withdraw(x, y)
    } yield b

    assert(f("10000", "7500") === Success(2500))

    f("10000", "deafbeef") match {
      case Failure(e: java.lang.NumberFormatException) =>
      case _ => fail("not throw NumberFormatException")
    }

    f("100", "101") match {
      case Failure(e: java.lang.Error) =>
        assert(e.getMessage == "balance is less than 101")
      case _ => fail("not throw java.lang.Error")
    }
  }
}