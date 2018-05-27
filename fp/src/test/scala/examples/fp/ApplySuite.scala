package examples.fp

import org.scalatest.FunSuite

class ApplySuite extends FunSuite {

  implicit def applyForMap[K] = new Apply[Map[K, ?]] {
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
}
