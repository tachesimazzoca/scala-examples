package example.rx

import scala.language.postfixOps
import scala.concurrent.duration._

import rx.lang.scala.Observable

object NestedExample {
  def mergeStreams[T](xs: Observable[(T, Duration)]): Observable[T] = {
    val yss: Observable[Observable[T]] =
      xs.map(x => Observable.interval(x._2).map(_ => x._1))
    yss.flatten
  }

  def concatinateStreams[T](xs: Observable[(T, Duration)],
      limit: Int): Observable[T] = {
    val yss: Observable[Observable[T]] =
      xs.map(x => Observable.interval(x._2).map(_ => x._1).take(limit))
    yss.concat
  }

  val fizzbuzz = mergeStreams(
    Observable(
      ("fizz", 3 seconds),
      ("buzz", 5 seconds),
      ("----", 1 seconds)
    )
  )
  val s1 = fizzbuzz.subscribe(println(_))
  println("fizzbuzz: Unsubscribe to prese ENTER ...")
  readLine()
  s1.unsubscribe()

  val concatinated = concatinateStreams(
    Observable(
      ("Foo", 3 seconds),
      ("Bar", 2 seconds),
      ("Baz", 1 seconds)
    ), 2
  )
  val s2 = concatinated.subscribe(println(_))
  println("concatinated: Unsubscribe to prese ENTER ...")
  readLine()
  s2.unsubscribe()
}
