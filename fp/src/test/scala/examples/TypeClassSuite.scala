package examples.fp

import org.scalatest.FunSuite

import scala.util.{Success, Try}

class TypeClassSuite extends FunSuite {

  import TypeClass.FlatMap
  import TypeClass.FlatMapOps

  case class User(id: Long, active: Boolean)

  def findUser(id: Long): Try[Option[User]] = Try(Some(User(123L, true)))

  def findTags(user: User): Try[Option[List[String]]] = Try(Some(List("scala", "java")))

  test("for-comprehension without type class") {
    val result: Try[Option[List[String]]] = for {
      maybeUser <- findUser(123L)
      maybeTags <- maybeUser match {
        case Some(a) => findTags(a)
        case None => Try(None)
      }
    } yield maybeTags
    assert(result === Success(Some(List("scala", "java"))))
  }

  test("for-comprehension with type class") {

    case class TryOption[A](value: Try[Option[A]])

    implicit val tryOptionF = new FlatMap[TryOption] {
      def map[A, B](fa: TryOption[A])(f: A => B): TryOption[B] = TryOption(fa.value.map(_.map(f)))

      def flatMap[A, B](fa: TryOption[A])(f: A => TryOption[B]): TryOption[B] =
        TryOption(fa.value.flatMap {
          case Some(a) => f(a).value
          case None => Try(None)
        })
    }

    val result = (for {
      user <- TryOption(findUser(123L))
      tags <- TryOption(findTags(user))
    } yield tags).value
    assert(result === Success(Some(List("scala", "java"))))
  }
}
