import scala.collection.Seq

ThisBuild / version := "0.0.0"

//ThisBuild / scalaVersion := "3.3.4"
ThisBuild /scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "movie-review-api"
  )

lazy val zioVersion = "2.1.14"

libraryDependencies ++= Seq(
    "dev.zio" %% "zio" % zioVersion,
    "dev.zio" %% "zio-test" % zioVersion,
    "dev.zio" %% "zio-test-sbt" % zioVersion,
    "dev.zio" %% "zio-streams" % zioVersion,
    "dev.zio" %% "zio-test-junit" % zioVersion,
    "dev.zio"  %% "zio-http" % "3.0.1",
    "com.google.firebase" % "firebase-admin" % "9.4.3",
    "com.google.cloud" % "google-cloud-firestore" % "3.30.4",
    "com.typesafe.play" %% "play-json" % "2.10.6"
)

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
