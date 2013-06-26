name := "scala-examples" 

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.1"

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "1.8" % "test"
)
