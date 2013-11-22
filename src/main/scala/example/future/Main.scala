package example.future

import scala.language.postfixOps

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

import scala.util.{Success, Failure}

object Main extends App {
  println("HelloExample")
  println("============")
  HelloExample

  Thread.sleep(4000L)
  println()

  println("QueueExample")
  println("============")
  QueueExample

  Thread.sleep(1000L)
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

  object SequenceExample {
    val fs = List(
      future { Thread.sleep(2000L); 1 },
      future { 2 },
      future { Thread.sleep(1000L); 3 }
    )
    def sequence[T](fs: List[Future[T]]): Future[List[T]] = {
      val p = promise[List[T]]
      p.success(Nil)
      fs.foldRight(p.future) { (f, acc) =>
        for { x <- f; xs <- acc } yield x :: xs
      }
    }

    println(Await.result(sequence(fs), 4 seconds))
  }
}
