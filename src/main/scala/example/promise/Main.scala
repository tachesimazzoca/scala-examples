package example.promise

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object Main extends App {
  println("PromiseExample")
  println("==============")
  PromiseExample

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

  object PromiseExample {
    val p = promise[String]

    val op1 = future {
      println("op1 start computation ...")
      p.success("op1 completes the promise!")
      println("op1 do more computation ...")
      Thread.sleep(2000L)
      println("op1 done")
    }

    val op2 = future {
      println("op2 start computation ...")
      p.future onSuccess {
        case x => println("op2 received - " + x)
      }
    }
  }

  private def randomWaitTask[T](x: T) =
    future {
      val msec = (math.random * 1000).toLong
      println(x.toString + " after " + msec + "msec")
      Thread.sleep(msec)
      x
    }

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
}
