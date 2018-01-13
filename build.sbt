organization := "onema"

name := "JsonCore"

version := "0.3.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {
  Seq(

    // Json De/Serializer
    "org.json4s"                 % "json4s-native_2.12"       % "3.5.3",

    // Logging
    "com.typesafe.scala-logging" %% "scala-logging"           % "3.7.2",
    "ch.qos.logback"             % "logback-classic"          % "1.1.7",

    // Testing
    "org.scalatest"             %% "scalatest"                % "3.0.0"       % "test"
  )
}

publishMavenStyle := true
publishTo := Some("Onema Snapshots" at "s3://s3-us-east-1.amazonaws.com/ones-deployment-bucket/snapshots")
