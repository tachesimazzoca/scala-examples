package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object RecoveryExample {
  val validOp: Future[Int] = future { 0 }
  val invalidOp1: Future[Int] = future { 1 / 0 }
  val invalidOp2: Future[Int] = future { List[Int]().head }

  invalidOp1 recover {
    case _ => 0
  } onSuccess { case x =>
    println("invalidOp1 recover 0: " + x)
  }

  invalidOp1 recoverWith {
    case _: ArithmeticException => validOp
  } onSuccess { case x =>
    println("invalidOp1 recoverWith validOp: " + x)
  }
  invalidOp2 recoverWith {
    case _: ArithmeticException => validOp
  } onFailure {
    case e => println("invalidOp2 not recovered: " + e.getClass)
  }

  invalidOp1 fallbackTo validOp onSuccess { case x =>
    println("invalidOp1 fallbackTo validOp: " + x)
  }
  invalidOp1 fallbackTo invalidOp2 onFailure { case e =>
    println("invalidOp1 fallbackTo invalidOp2: " + e.getMessage)
  }
}
