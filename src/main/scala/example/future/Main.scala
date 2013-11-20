package example.future

import scala.concurrent._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object Main extends App {
  HelloExample
  Thread.sleep(4000L)
  QueueExample
  Thread.sleep(1000L)
  MonadExample
  Thread.sleep(1000L)
  RecoveryExample
  Thread.sleep(1000L)

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

  object QueueExample {
    @volatile var queue = List("Foo", "Bar", "Baz")

    val dequeue: Future[String] = future {
      println("dequeue ...")
      val v = queue.head
      queue = queue.tail
      v
    }
    for (n <- 1 to 5) {
      dequeue onSuccess { case x => println(n + ":" + x) }
    }
  }

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

  object RecoveryExample {
    val validOp: Future[Int] = future { 0 }
    val invalidOp1: Future[Int] = future { 1 / 0 }
    val invalidOp2: Future[Int] = future { List[Int]().head }

    invalidOp1 recover {
      case _ => 0
    } onSuccess { case x =>
      println("invalidOp1 recover 0: " + x)
    }

    invalidOp1 recoverWith {
      case _: ArithmeticException => validOp
    } onSuccess { case x =>
      println("invalidOp1 recoverWith validOp: " + x)
    }
    invalidOp2 recoverWith {
      case _: ArithmeticException => validOp
    } onFailure {
      case e => println("invalidOp2 not recovered: " + e.getClass)
    }

    invalidOp1 fallbackTo validOp onSuccess { case x =>
      println("invalidOp1 fallbackTo validOp: " + x)
    }
    invalidOp1 fallbackTo invalidOp2 onFailure { case e =>
      println("invalidOp1 fallbackTo invalidOp2: " + e.getMessage)
    }
  }
}
