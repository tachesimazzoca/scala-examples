package example

package object promise {
  import scala.concurrent._
  import ExecutionContext.Implicits.global

  def randomWaitTask[T](x: T): Future[T] =
    future {
      val msec = (math.random * 1000).toLong
      println(x.toString + " after " + msec + "msec")
      Thread.sleep(msec)
      x
    }
}
