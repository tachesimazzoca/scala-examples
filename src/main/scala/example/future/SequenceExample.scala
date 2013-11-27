package example.future

import scala.language.postfixOps

import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

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
