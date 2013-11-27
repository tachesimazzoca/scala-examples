package example.promise

import scala.concurrent._
import ExecutionContext.Implicits.global

object Main extends App {
  println("PromiseExample")
  println("==============")
  HelloExample
  Thread.sleep(3000L)
  println()

  println("CompleteExample")
  println("===============")
  CompleteExample
  Thread.sleep(1000L)
  println()

  println("TryCompleteExample")
  println("==================")
  TryCompleteExample
  Thread.sleep(1000L)
}
