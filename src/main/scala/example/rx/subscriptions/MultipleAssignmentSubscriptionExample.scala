package example.rx.subscriptions

import rx.lang.scala.Subscription
import rx.lang.scala.subscriptions.MultipleAssignmentSubscription

/** MultipleAssignmentSubscription represents a Subscription
 * whose underlying subscription can be swapped for another
 * subscription.
 */
object MultipleAssignmentSubscriptionExample {
  val a = Subscription { println("a - bye") }
  val b = Subscription { println("b - bye") }
  val c = Subscription { println("c - bye") }
  val s = MultipleAssignmentSubscription()

  s.subscription = a
  s.subscription = b

  println("s.unsubscribe() to prese ENTER ...")
  readLine()
  s.unsubscribe()
  println(s"a.isUnsubscribed: ${a.isUnsubscribed} -> swapped already")
  println(s"b.isUnsubscribed: ${b.isUnsubscribed} -> assigned")
  println(s"c.isUnsubscribed: ${c.isUnsubscribed} -> not assigned")
  println(s"s.isUnsubscribed: ${s.isUnsubscribed}")

  println(s"`s.subscription = c` to prese ENTER ...")
  readLine()
  s.subscription = c
  println(s"a.isUnsubscribed: ${a.isUnsubscribed} -> swapped already")
  println(s"c.isUnsubscribed: ${c.isUnsubscribed} -> unsubscribed immediately")

  println("a.unsubscribe() to prese ENTER ...")
  readLine()
  a.unsubscribe()
  println(s"a.isUnsubscribed: ${a.isUnsubscribed}")
}
