package example.collection

object Main extends App {
  // See scala.collection.Traversable

  // foreach[U](f: A => U): Unit
  val messages: List[String] = List("Foo", "Bar", "Fuga")
  messages.foreach(println)
  // same as for
  for (i <- 0 until messages.size) {
    println(messages(i))
  }

  // ++[B >: A, That](that: GenTraversableOnce[B])
  // (implicit bf: CanBuildFrom[Repr, B, That]): That
  println(List("1st", "2nd") ++ List("3rd", "4th"))
  println(Set("1st", "2nd") ++ Set("3rd", "1st"))
  println(Map('foo -> "Foo", 'bar -> "Bar") ++ Map('foo -> "Fuga"))

  // map[B, That](f: A => B)
  // (implicit bf: CanBuildFrom[Traversable[A], B, That]): That
  println(List(1, 2, 3).map { _ + 1 })

  // exists(p: A => Boolean): Boolean
  println("List(1, 2, 3) exists 0 ...")
  println(List(1, 2, 3) exists { _ == 0 })
  println("List(1, 2, 3) exists > 0 ...")
  println(List(1, 2, 3) exists { _ > 0 })
  println("List(2, 4, 6) exists even ...")
  println(List(2, 4, 6) exists { _ % 2 == 0 })

  // forall(p: A => Boolean): Boolean
  println("List(1, 2, 3) forall > 0 ...")
  println(List(1, 2, 3) forall { _ > 0 })
  println("List(1, 2, 3) forall > 1 ...")
  println(List(1, 2, 3) forall { _ > 1 })
  println("List(2, 4, 6) forall even ...")
  println(List(2, 4, 6) forall { _ % 2 == 0 })
  println("List(2, 3, 6) forall even ...")
  println(List(2, 3, 6) forall { _ % 2 == 0 })
}
