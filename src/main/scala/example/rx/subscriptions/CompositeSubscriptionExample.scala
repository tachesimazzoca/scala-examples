package example.rx.subscriptions

import rx.lang.scala.subscriptions._

/** CompositeSubscription represents a group of subscriptions
 * that are disposed together.
 */
object CompositeSubscriptionExample {
  val a = Subscription { println("a - bye") }
  val b = Subscription { println("b - bye") }
  val c = Subscription { println("c - bye") }
  val s = CompositeSubscription(a, b)

  println("s.unsubscribe() to prese ENTER ...")
  readLine()
  s.unsubscribe()
  println(s"a.isUnsubscribed: ${a.isUnsubscribed}")
  println(s"b.isUnsubscribed: ${b.isUnsubscribed}")
  println(s"c.isUnsubscribed: ${c.isUnsubscribed} -> not grouped")
  println(s"s.isUnsubscribed: ${s.isUnsubscribed}")

  println("`s += c` to prese ENTER ...")
  readLine()
  s += c
  println(s"c.isUnsubscribed: ${c.isUnsubscribed} -> unsubscribed immediately")

  println()
}
