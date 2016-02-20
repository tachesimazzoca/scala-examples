package example.lang

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
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
    override def sound: String = "call"
  }

  class Chicken extends Bird {
    override def sound: String = "cluck"
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

  test("lower bound - Node") {
    case class Node[+T](head: T, tail: Node[T]) {
      def prepend[U >: T](elem: U): Node[U] = Node(elem, this)
    }

    val chickens: Node[Chicken] = Node(new Chicken, null)
    val birds: Node[Bird] = chickens.prepend(new Bird)
    val animals: Node[Animal] = birds.prepend(new Animal)
    //val reborn: Node[Bird] = animals.prepend(new Bird) // not compile
    assert(animals.head.sound === "rustle")
    assert(animals.tail.head.sound === "call")
    assert(animals.tail.tail.head.sound === "cluck")
  }

  test("lower bound - Stack") {
    class Stack[+A] {
      def push[B >: A](elem: B): Stack[B] = new Stack[B] {
        override def top: B = elem

        override def pop: Stack[B] = Stack.this
      }

      def top: A = throw new NoSuchElementException

      def pop: Stack[A] = throw new NoSuchElementException
    }

    val chickens = new Stack[Chicken].push(new Chicken)
    val birds: Stack[Bird] = chickens.push(new Bird)
    val animals: Stack[Animal] = birds.push(new Animal)
    //val reborn: Node[Bird] = animals.push(new Bird) // not compile
    assert(animals.top.sound === "rustle")
    assert(animals.pop.top.sound === "call")
    assert(animals.pop.pop.top.sound === "cluck")
  }
}
