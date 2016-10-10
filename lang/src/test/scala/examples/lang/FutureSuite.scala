package examples.lang

import org.scalatest.FunSuite

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

class FutureSuite extends FunSuite {
  test("onComplete") {
    def helloAfter(delay: Long): Future[String] = Future {
      println(s"helloAfter(${delay} msec)")
      Thread.sleep(delay)
      s"Hello after ${delay} msec"
    }

    Seq(2000L, 3000L, -1L, 1000L).foreach(helloAfter(_).onComplete {
      case Success(msg) =>
        println(msg)
      case Failure(e) =>
        println("error: " + e.getMessage)
    })

    Thread.sleep(4000L)
  }

  test("for-comprehension") {
    val futureInt1: Future[Int] = Future {
      1
    }
    val futureInt2: Future[Int] = Future {
      2
    }
    val futureError: Future[Int] = Future {
      throw new ArithmeticException
    }

    assert(3 === Await.result(futureInt1.flatMap { x =>
      futureInt2.map(y => x + y)
    }, Duration.Inf))

    assert(3 === Await.result(for {
      x <- futureInt1
      y <- futureInt2
    } yield {
      x + y
    }, Duration.Inf))

    assert(Try(Await.result(for {
      x <- futureInt1
      y <- futureInt2
      z <- futureError
    } yield {
      x + y + z
    }, Duration.Inf)).isFailure)
  }

  test("andThen") {
    def tailFuture(acc: List[String]): Future[List[String]] = Future {
      acc.tail
    } andThen {
      case Success(xs) => "andThen" :: xs // doesn't affect the return value
      case Failure(e) => println(e.getMessage)
    }

    assert(List("bar", "baz") ===
      Await.result(tailFuture(List("foo", "bar", "baz")), Duration.Inf))
    assertThrows[UnsupportedOperationException] {
      Await.result(tailFuture(Nil), Duration.Inf)
    }
  }

  test("zip for parallel execution") {
    def waitFor(delay: Long): Future[Long] = Future {
      println(s"waitFor(${delay} msec)")
      Thread.sleep(delay)
      delay
    }

    assert((2000L, 1000L) === Await.result(waitFor(2000L) zip waitFor(1000L), Duration.Inf))

    assert((2000L, 1000L) === Await.result(for {
      x <- waitFor(2000L)
      y <- waitFor(1000L)
    } yield {
      (x, y)
    }, Duration.Inf))
  }

  test("sequence") {
    def sequence[T](fs: List[Future[T]]): Future[List[T]] =
      fs.foldRight(Future.successful[List[T]](Nil)) { (f, acc) =>
        for {
          x <- f
          xs <- acc
        } yield {
          x :: xs
        }
      }

    assert(List(1, 2, 3) === Await.result(sequence(
      List(
        Future {
          Thread.sleep(2000L);
          1
        },
        Future {
          2
        },
        Future {
          Thread.sleep(1000L);
          3
        }
      )), Duration.Inf))
  }

  test("recovery") {
    val zeroF: Future[Int] = Future {
      0
    }
    val divideByZeroF: Future[Int] = Future {
      1 / 0 // throws ArithmeticException
    }
    val emptyHeadF: Future[Int] = Future {
      List.empty[Int].head // throws NoSuchElementException
    }

    assert(0 === Await.result(divideByZeroF recover {
      case _: ArithmeticException => 0
    }, Duration.Inf))

    assert(0 === Await.result(divideByZeroF recoverWith {
      case _: ArithmeticException => zeroF
    }, Duration.Inf))

    assert(0 === Await.result(emptyHeadF recoverWith {
      case _: NoSuchElementException => zeroF
    }, Duration.Inf))

    assertThrows[NoSuchElementException] {
      Await.result(emptyHeadF recoverWith {
        case _: ArithmeticException => zeroF
      }, Duration.Inf)
    }

    assert(0 === Await.result(divideByZeroF fallbackTo zeroF, Duration.Inf))

    assertThrows[ArithmeticException] {
      Await.result(divideByZeroF fallbackTo emptyHeadF, Duration.Inf)
    }
  }
}
