package examples.lang

import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Success, Try}

class PromiseSuite extends FunSuite {

  object Ajax {
    def get(url: String, ok: (String) => Unit, ng: (Throwable) => Unit): Unit = {
      if (url.startsWith("http://")) ok("OK")
      else ng(new UnsupportedOperationException("Unsupported protocol"))
    }
  }

  test("success/failure") {
    def fetchContent(url: String): Future[String] = {
      val p = Promise[String]()
      Ajax.get(url, p.success(_), p.failure(_))
      p.future
    }

    Seq("http://example.net", "ftp://example.net") foreach { url =>
      val f = fetchContent(url)
      f onComplete {
        case Success(x) => println(x)
        case Failure(e) => println(e.getMessage)
      }
      Await.ready(f, Duration.Inf)
    }
  }

  test("Promise completes only once") {
    def completeAfter(p: Promise[String], delay: Long): Future[Unit] = Future {
      println(s"completeAfter(${delay} msec)")
      Thread.sleep(delay)
      p.complete(Success(s"Completed after ${delay} msec"))
    }

    val p = Promise[String]()
    val firstF = p.future
    firstF onComplete {
      case Success(msg) => println(msg)
      case Failure(e) => println(e.getMessage)
    }

    val f1 = completeAfter(p, 1000L)
    val f2 = completeAfter(p, 2000L)
    f2 onFailure {
      case e => println(e)
    }

    Await.ready(firstF, Duration.Inf)
    Await.ready(f1, Duration.Inf)
    Await.ready(f2, Duration.Inf)
  }

  test("tryComplete") {
    val p = Promise[String]()
    assert(true === p.trySuccess("The 1st attempt should succeed"))
    assert(false === p.trySuccess("The 2nd attempt should fail"))
  }
}
