package example.dao

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
    val id = Value(user.id.getOrElse(1))
    val createdAt = Value(user.createdAt.getOrElse(ctx.time))
    val updatedAt = Value(ctx.time)

    if (id.get < 1) Left("User.id is out of range.")
    else Right(User(id, user.name, createdAt, updatedAt))
  }
}
