package example.rx.subscriptions

import rx.lang.scala.subscriptions._

object BooleanSubscriptionExample {
  val s = BooleanSubscription { println("bye") }
  println("Unsubscribe to prese ENTER ...")
  println(s.isUnsubscribed)
  readLine()
  s.unsubscribe()
  println(s.isUnsubscribed)
}
