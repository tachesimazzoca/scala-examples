name := "scala-examples"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-actors" % "2.10.2",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)
