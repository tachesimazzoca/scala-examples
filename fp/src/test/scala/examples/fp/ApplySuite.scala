package examples.fp

import org.scalatest.FunSuite

class ApplySuite extends FunSuite {

  implicit def applyForMap[K]: Apply[Map[K, ?]] = new Apply[Map[K, ?]] {
    override def ap[A, B](ff: Map[K, A => B])(fa: Map[K, A]): Map[K, B] = fa.flatMap { case (k, v) =>
      ff.get(k).map(f => (k, f(v)))
    }

    override def map[A, B](fa: Map[K, A])(f: A => B): Map[K, B] = fa.mapValues(f)
  }

  test("applyForMap") {
    val F = Apply[Map[Symbol, ?]]

    val removeWhitespace: Option[String] => Option[String] = _.map(_.trim).filter(!_.isEmpty)
    val toLower: Option[String] => Option[String] = _.map(_.toLowerCase)

    val normalization: Map[Symbol, Option[String] => Option[String]] = Map(
      'username -> removeWhitespace.andThen(toLower),
      'password -> identity,
      'url -> removeWhitespace
    )

    assert(F.ap(normalization)(Map(
      'username -> Some("user1@Example.net  "),
      'password -> Some(" deadbeef "),
      'url -> Some(" ")
    )) === Map(
      'username -> Some("user1@example.net"),
      'password -> Some(" deadbeef "),
      'url -> None
    ))

    assert(F.map(Map(
      'username -> Some("user1@Example.net  "),
      'password -> Some(" dead beef "),
      'url -> Some(" ")
    ))(removeWhitespace) === Map(
      'username -> Some("user1@Example.net"),
      'password -> Some("dead beef"),
      'url -> None
    ))
  }

  implicit def applyForOption: Apply[Option] = new Apply[Option] {
    override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = ff.flatMap(fa.map(_))

    override def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  }

  test("applyForOption") {
    val F = Apply[Option]

    val removeWhitespace: Option[String => String] = Some(_.trim)

    assert(F.ap(removeWhitespace)(None) === None)
    assert(F.ap(removeWhitespace)(Some(" foo ")) === Some("foo"))
  }
}
