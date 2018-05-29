name := "scala-examples-cats"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-Ypartial-unification"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
