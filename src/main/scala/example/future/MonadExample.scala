package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object MonadExample {
  val validOp1: Future[Int] = future { 1 }
  val validOp2: Future[Int] = future { 2 }
  val invalidOp: Future[Int] = future { 2 / 0 }

  val usingMap: Future[Int] = validOp1 flatMap {
    a => validOp2 map { b => a + b } }
  usingMap onSuccess { case x => println("usingMap: " + x) }

  val usingFor: Future[Int] = for {
    a <- validOp1
    b <- validOp2
  } yield a + b
  usingFor onSuccess { case x => println("usingFor: " + x) }

  val usingFor2: Future[Int] = for {
    a <- validOp1
    b <- validOp2
    c <- invalidOp
  } yield a + b + c
  usingFor2 onFailure { case e => println("usingFor2: " + e.getMessage) }
}
