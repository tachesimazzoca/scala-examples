package example.promise

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.Success

object CompleteExample {
  val p = promise[Int]

  for (n <- 1 to 2) {
    randomWaitTask(n) onSuccess {
      // Promise completes only once.
      // The others should fail with IllegalStateException
      case x => p.complete(Success(x))
    }
  }
  p.future onSuccess {
    case x => println("promise completed - " + x)
  }
}
