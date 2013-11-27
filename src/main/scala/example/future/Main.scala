package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

object Main extends App {
  println("HelloExample")
  println("============")
  HelloExample
  Thread.sleep(4000L)
  println()

  println("MonadExample")
  println("============")
  MonadExample
  Thread.sleep(1000L)
  println()

  println("RecoveryExample")
  println("===============")
  RecoveryExample
  Thread.sleep(1000L)
  println()

  println("AndThenExample")
  println("==============")
  AndThenExample
  Thread.sleep(1000L)
  println()

  println("SequenceExample")
  println("===============")
  SequenceExample
}
