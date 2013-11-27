package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object HelloExample {
  def helloAfter(delay: Long): Future[String] = future {
    println("helloAfter(" + delay + ") ...")
    Thread.sleep(delay)
    "Hello after " + delay + "msec"
  }

  Seq(2000L, 3000L, -1L, 1000L) foreach { delay =>
    helloAfter(delay) onComplete {
      case Success(x) => println(x)
      case Failure(e) => println("failure: " + e.getMessage)
    }
  }
}
