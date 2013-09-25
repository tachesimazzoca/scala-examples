package example.actor

import scala.actors.Actor
import scala.actors.Actor._

object Main extends App {
  val sleeper = new NonBlockingSleeper
  sleeper.start()

  sleeper ! (PrintRequest("Hello1"), 2000L)
  println("Sent Hello1 ....")
  sleeper ! (PrintRequest("Hello2"), 1000L)
  println("Sent Hello2 ....")

  sleeper ! (StopRequest("Bye ...."), 5000L)
}

abstract class SleepRequest(val msg: String)
case class StopRequest(override val msg: String) extends SleepRequest(msg)
case class PrintRequest(override val msg: String) extends SleepRequest(msg)

class NonBlockingSleeper extends Actor {
  def later(req: SleepRequest, wait: Long) {
    val me = self
    actor {
      Thread.sleep(wait)
      me ! req
    }
  }

  def act() {
    loop {
      react {
        case StopRequest(msg: String) => {
          println(msg)
          exit
        }
        case PrintRequest(msg: String) => {
          println(msg)
        }
        case (req: SleepRequest, wait: Long) => {
          later(req, wait)
        }
      }
    }
  }
}
