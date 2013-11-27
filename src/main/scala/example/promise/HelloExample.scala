package example.promise

import scala.concurrent._
import ExecutionContext.Implicits.global

object HelloExample {
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
