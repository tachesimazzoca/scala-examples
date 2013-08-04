package com.github.tachesimazzoca.scala.example

sealed abstract class Field[+T] {
  def toOption: Option[T] = this match {
    case Value(x) => Some(x)
    case Null => None
  }

  def get: T = toOption.get
  def getOrElse[V >: T](v: V): V = toOption.getOrElse(v)
}
case class Value[T](v: T) extends Field[T]
case object Null extends Field[Nothing]

case class User(
  id: Field[Int],
  name: Field[String],
  createdAt: Field[Long],
  updatedAt: Field[Long]
)

object UserDao {
  def save(user: User)(implicit ctx: Context): Either[String, User] = {
    val id = user.id match {
      case Null => Value(1)
      case m => m
    }
    val createdAt = user.createdAt match {
      case Null => Value(ctx.time)
      case m => m
    }
    val updatedAt = Value(ctx.time)

    if (id.get < 1)
      Left("User.id is out of range.")
    else
      Right(User(id, user.name, createdAt, updatedAt))
  }
}

case class Context(time: Long)

object DaoExample {
  implicit val context = Context(System.currentTimeMillis)

  def main(args: Array[String]) {
    System.out.println(UserDao.save(User(Null, Value("foo"), Null, Null)))
    System.out.println(UserDao.save(User(Value(-1), Value("bar"), Null, Null)))
    System.out.println(UserDao.save(User(Value(2), Value("baz"), Value(1234), Value(1234))))
    val ctx = Context(3456)
    System.out.println(UserDao.save(User(Value(3), Value("qux"), Value(1234), Value(1234)))(ctx))
  }
}
