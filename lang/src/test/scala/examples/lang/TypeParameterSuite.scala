package examples.lang

import org.scalatest.FunSuite

import scala.annotation.tailrec

class TypeParameterSuite extends FunSuite {

  test("variance") {
    class Covariant[+A]
    class Contravariant[-A]
    class Invariant[A]

    val a: Covariant[AnyRef] = new Covariant[String]
    val b: Contravariant[String] = new Contravariant[AnyRef]
    val c: Invariant[String] = new Invariant[String]
  }

  test("Array[T] is invariant") {
    val xs: Array[Int] = Array(1, 2, 3)
    // Array[Int] doesn't conform to expected type Array[Any]
    //val ys: Array[Any] = xs // not compile
  }

  test("List[+A] is covariant") {
    val xs: List[Number] = List(1, 2, 3)
    val ys: List[Any] = xs
    // List[Number] doesn't conform to expected type Array[Int]
    //val ys: List[Int] = xs // not compile
  }

  class Animal {
    def sound: String = "rustle"
  }

  class Bird extends Animal {
    override def sound: String = call

    def call: String = "call"
  }

  class Chicken extends Bird {
    override def call: String = cluck

    def cluck: String = "cluck"
  }

  test("trait Function1[-T1, +R] extends AnyRef") {
    val helloAnimal = (x: Animal) => x.sound
    assert(helloAnimal(new Animal) === "rustle")
    assert(helloAnimal(new Bird) === "call")
    assert(helloAnimal(new Chicken) === "cluck")

    val helloBird = (x: Bird) => x.sound
    //assert(helloBird(new Animal) === "rustle") // not compile
    assert(helloBird(new Bird) === "call")
    assert(helloBird(new Chicken) === "cluck")

    val helloChicken = (x: Chicken) => x.sound
    //assert(helloChicken(new Animal) === "rustle") // not compile
    //assert(helloChicken(new Bird) === "call") // not compile
    assert(helloChicken(new Chicken) === "cluck")

    val animal: String => Animal = {
      case "Bird" => new Bird
      case "Chicken" => new Chicken
      case _ => new Animal
    }
    assert(animal("Bird").sound === "call")
    assert(animal("Chicken").sound === "cluck")
    assert(animal("Foo").sound === "rustle")
  }

  test("upper bound") {
    def animalSounds[T <: Animal](xs: T*): Seq[String] = xs map (_.sound)
    assert(animalSounds(new Animal, new Bird, new Chicken) === Seq("rustle", "call", "cluck"))

    def birdSounds[T <: Bird](xs: T*): Seq[String] = xs map (_.sound)
    assert(birdSounds(new Bird, new Chicken) === Seq("call", "cluck"))
  }

  test("covariant Node[+T]") {
    case class Node[+T](head: T, tail: Node[T]) {
      def prepend[U >: T](elem: U): Node[U] = Node(elem, this)

      def root: T = {
        @tailrec
        def f(x: Node[T]): T = x match {
          case Node(a, null) => a
          case Node(a, b) => f(b)
        }
        f(this)
      }
    }

    // Node[Chicken]
    val chickens = Node(new Chicken, null)
    val chicken = chickens.root
    assert(chicken.cluck === "cluck")
    assert(chicken.call === "cluck")
    assert(chicken.sound === "cluck")

    // Node[Bird]
    val birds = chickens.prepend(new Bird)
    val chickenAsBird = birds.root
    //assert(chickenAsBird.cluck === "cluck") // not compile
    assert(chickenAsBird.call === "cluck")
    assert(chickenAsBird.sound === "cluck")

    // Node[Animal]
    val animals = birds.prepend(new Animal)
    val chickenAsAnimal = animals.root
    //assert(chickenAsAnimal.cluck === "cluck") // not compile
    //assert(chickenAsAnimal.call === "cluck") // not compile
    assert(chickenAsAnimal.sound === "cluck")

    // Node[Bird]?
    //val reborn: Node[Bird] = animals.prepend(new Bird) // not compile
  }

  class Response(val message: String)

  class HeaderResponse(
    override val message: String,
    val header: List[String]) extends Response(message)

  class BodyResponse(
    override val message: String,
    override val header: List[String],
    val body: Array[Byte]) extends HeaderResponse(message, header)

  abstract class Bag[T] {
    def add(x: T): Bag[T]

    def toSeq: Seq[T]
  }

  object Bag {
    def apply[T](xs: T*): Bag[T] = new BagImpl(xs.reverse.toList)

    private class BagImpl[T](val xs: List[T]) extends Bag[T] {
      override def add(x: T): Bag[T] = new BagImpl(x :: xs)

      override def toSeq: Seq[T] = xs.reverse
    }

  }

  test("invariant Bag[T]") {

    val forbidden = new Response(
      "HTTP/1.1 403 Forbidden")
    val seeOther = new HeaderResponse(
      "HTTP/1.1 303 See Other",
      List("Location: another.example.net"))
    val ok = new BodyResponse(
      "HTTP/1.1 200 OK",
      List("Content-Type: text/plain"),
      "Hello".getBytes)

    // Bag[Response]
    val responseStack = Bag(ok, seeOther, forbidden)
    val response = responseStack.toSeq.head
    assert(ok.message === response.message)
    //assert(ok.header === response.header) // not compile
    //assert(ok.body === response.body) // not compile
    responseStack.add(ok)
    responseStack.add(seeOther)
    responseStack.add(forbidden)

    // Bag[HeaderResponse]
    val headerResponseStack = Bag(ok, seeOther)
    val headerResponse = headerResponseStack.toSeq.head
    assert(ok.message === headerResponse.message)
    assert(ok.header === headerResponse.header)
    //assert(ok.body === headerResponse.body) // not compile
    headerResponseStack.add(ok)
    headerResponseStack.add(seeOther)
    //headerResponseStack.add(forbidden) // not compile

    // Bag[BodyResponse]
    val bodyResponseStack = Bag(ok)
    val bodyResponse = bodyResponseStack.toSeq.head
    assert(ok.message === bodyResponse.message)
    assert(ok.header === bodyResponse.header)
    assert(ok.body === bodyResponse.body)
    bodyResponseStack.add(ok)
    //bodyResponseStack.add(seeOther) // not compile
    //bodyResponseStack.add(forbidden) // not compile
  }
}
