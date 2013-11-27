package example.promise

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.Success

object TryCompleteExample {
  val p = promise[Int]

  for (n <- 1 to 2) {
    randomWaitTask(n) onSuccess {
      case x =>
        // Promise#tryComplete returns Boolean instead of the exception
        if (!p.tryComplete(Success(x))) {
          println("tryComplete failed - " + x)
        }
    }
  }
  p.future onSuccess {
    case x => println("promise completed - " + x)
  }
}
