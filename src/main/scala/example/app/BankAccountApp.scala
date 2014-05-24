package example.app

import scala.util.{Try, Success, Failure}

object BankAccountApp {
  def withdraw(balance: Int, amount: Int): Try[Int] = {
    val x = balance - amount
    if (x >= 0) Success(x)
    else Failure(new Error("balance is less than " + x))
  }

  def main(args: Array[String]) {
    val result = for {
      a <- Try(args(0).toInt)
      b <- Try(args(1).toInt)
      balance <- withdraw(a, b)
    } yield s"balance: ${balance}"
    println(result)
  }
}
