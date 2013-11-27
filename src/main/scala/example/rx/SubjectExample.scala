package example.rx

import rx.lang.scala.Subject
import rx.lang.scala.subjects._

object SubjectExample {
  def testChannel(channel: Subject[Int, Int]) {
    val a = channel.subscribe(
      x  => println(s"a: ${x}"),
      e  => println(s"e: ${e}"),
      () => println(s"a: completed")
    )
    val b = channel.subscribe(
      x  => println(s"b: ${x}"),
      e  => println(s"b: ${e}"),
      () => println(s"b: completed")
    )

    channel.onNext(1)
    a.unsubscribe()
    channel.onNext(2)
    channel.onCompleted()

    val c = channel.subscribe(
      x  => println(s"c: ${x}"),
      e  => println(s"c: ${e}"),
      () => println(s"c: completed")
    )
    channel.onNext(3)
    channel.onCompleted()
  }

  println("PublishSubject")
  println("--------------")
  testChannel(PublishSubject[Int](0))
  println()

  println("ReplaySubject")
  println("--------------")
  testChannel(ReplaySubject[Int]())
  println()

  println("BehaviorSubject")
  println("--------------")
  testChannel(BehaviorSubject(0))
  println()

  println("AsyncSubject")
  println("--------------")
  testChannel(AsyncSubject[Int]())
  println()
}
