organization := "io.onema"

name := "json-extensions"

version := "0.1.0"

scalaVersion := "2.12.5"

libraryDependencies ++= {
  Seq(

    // Json De/Serializer
    "org.json4s"                 %% "json4s-jackson"          % "3.5.3",

    // Testing
    "org.scalatest"             %% "scalatest"                % "3.0.0"       % "test"
  )
}

publishMavenStyle := true
publishTo := Some("Onema Snapshots" at "s3://s3-us-east-1.amazonaws.com/ones-deployment-bucket/snapshots")
