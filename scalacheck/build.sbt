name := "scala-examples-scalacheck"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test",
  "org.scalacheck" % "scalacheck_2.11" % "1.13.2" % "test"
)
