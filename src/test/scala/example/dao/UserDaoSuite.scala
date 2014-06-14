package example.dao

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserDaoSuite extends FunSuite {
  implicit val context = Context(System.currentTimeMillis)

  test("save") {
    assert(Right(User(Value(1), Value("foo"),
      Value(context.time), Value(context.time))) ===
      UserDao.save(User(Null, Value("foo"), Null, Null)))

    assert(Left("User.id is out of range.") ===
      UserDao.save(User(Value(-1), Value("bar"), Null, Null)))

    assert(Right(User(Value(2), Value("baz"),
      Value(1234), Value(context.time))) ===
      UserDao.save(User(Value(2), Value("baz"), Value(1234), Value(3456))))

    val ctx = Context(4567)
    assert(Right(User(Value(3), Value("qux"), Value(1234), Value(4567))) ===
      UserDao.save(User(Value(3), Value("qux"), Value(1234), Value(3456)))(ctx))
  }
}
