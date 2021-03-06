name := "scala-examples-fp"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-Ypartial-unification"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

resolvers += Resolver.sonatypeRepo("releases")

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
