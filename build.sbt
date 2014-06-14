name := "scala-examples"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.2"

org.scalastyle.sbt.ScalastylePlugin.Settings

org.scalastyle.sbt.PluginKeys.config <<= baseDirectory {
  _ / "etc" / "scalastyle" / "scalastyle-config.xml"
}

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-actors" % "2.10.2",
  "com.netflix.rxjava" % "rxjava-scala" % "0.15.1",
  "junit" % "junit" % "4.10" % "test",
//  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
//  "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
  "org.scalatest" % "scalatest_2.10" % "2.1.5" % "test",
  "org.scalacheck" % "scalacheck_2.10" % "1.11.4" % "test"
)
