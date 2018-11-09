organization := "io.onema"

name := "json-extensions"

version := "0.3.1"

scalaVersion := "2.12.6"

libraryDependencies ++= {
  Seq(

    // Json De/Serializer
    "org.json4s"                     %% "json4s-jackson"             % "3.6.0",
    "com.fasterxml.jackson.datatype" % "jackson-datatype-joda"       % "2.9.6",

    // Testing
    "org.scalatest"                  %% "scalatest"                  % "3.0.0"       % "test"
  )
}

// Maven Central Repo boilerplate configuration
pomIncludeRepository := { _ => false }
licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url("https://github.com/onema/JsonExtensions"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/onema/JsonExtensions"),
    "scm:git@github.com:onema/JsonExtensions.git"
  )
)
developers := List(
  Developer(
    id    = "onema",
    name  = "Juan Manuel Torres",
    email = "software@onema.io",
    url   = url("https://github.com/onema/")
  )
)
publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
publishArtifact in Test := false
