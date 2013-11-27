package example.rx

import scala.language.postfixOps
import scala.concurrent.duration._

import rx.lang.scala.{Observable, Subscription}

object HelloExample {
  val ticks: Observable[Long] = Observable.interval(1 second)
  val evens: Observable[Long] = ticks.filter(_ % 2 == 0)
  val buffers: Observable[Seq[Long]] = evens.buffer(count = 3, skip = 1)

  val s1: Subscription = ticks.subscribe(x => println("ticks: " + x))
  val s2: Subscription = buffers.subscribe(x => println("even buffers: " + x))

  println("Unsubscribe even buffers to press ENTER key ...")
  readLine()
  s2.unsubscribe()

  println("Unsubscribe ticks to press ENTER key ...")
  readLine()
  s1.unsubscribe()
}
