package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object AndThenExample {
  val items = collection.mutable.ListBuffer("Foo", "Bar")
  future {
    if ((math.random * 2).toInt == 0) List("Baz")
    else Nil.tail
  } andThen {
    case Success(xs) => (items ++= xs)
    case Failure(e) => println("andThen failure - " + e.getMessage)
  } andThen {
    case _ => items ++= List("Fuga")
  } onComplete {
    case _ => println(items)
  }
}
