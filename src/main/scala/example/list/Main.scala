package example.list

object Main extends App {
  // Range#toList
  println(Range(1, 10, 3).toList)
  println((0 until 10).toList)

  // Cons ::(x: A): List[A]
  println("1st" :: List("2nd", "3rd"))
  // Functions ending in : are right-associative.
  object Hello {
    def ::(str: String) { println(str + " says Hello.") }
  }
  "Foo" :: Hello  // Foo says Hello.
  Hello.::("Bar") // Bar says Hello.

  // Pattern match
  for (ls <- List(List(1, 2, 3), List())) {
    ls match {
      case Nil => println("isEmpty")
      case x :: xs => println(x + ", " + xs)
    }
  }

  // Recursion
  def sum(ls: List[Int]): Int = ls match {
    case Nil => 0
    case x :: xs => x + sum(xs)
  }
  println(sum(List(1, 2, 3)))
}
