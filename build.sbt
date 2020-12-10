import scoverage.ScoverageKeys.coverageMinimum

organization := "io.laserdisc"
name         := "fs2-aws"

lazy val scala212               = "2.12.12"
lazy val scala213               = "2.13.3"
lazy val supportedScalaVersions = List(scala212, scala213)

crossScalaVersions in ThisBuild := supportedScalaVersions

scalaVersion in ThisBuild := scala213

lazy val root = (project in file("."))
  .aggregate(
    `fs2-aws`,
    `fs2-aws-s3`,
    `fs2-aws-sqs`,
    `fs2-aws-sqs-testkit`,
    `fs2-aws-testkit`,
    `fs2-aws-dynamodb`,
    `fs2-aws-core`,
    `fs2-aws-examples`,
    `fs2-aws-ciris`
  )
  .settings(
    publishArtifact    := false,
    crossScalaVersions := Nil
  )

lazy val `fs2-aws-core` = (project in file("fs2-aws-core"))
  .settings(
    name := "fs2-aws-core",
    libraryDependencies ++= Seq(
      "co.fs2"        %% "fs2-core"                % V.Fs2,
      "co.fs2"        %% "fs2-io"                  % V.Fs2,
      "org.mockito"   % "mockito-core"             % V.MockitoCore % Test,
      "org.mockito"   %% "mockito-scala-scalatest" % V.MockitoScalaTest % Test,
      "org.scalatest" %% "scalatest"               % V.ScalaTest % Test
    ),
    coverageMinimum       := 40,
    coverageFailOnMinimum := true
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-ciris` = (project in file("fs2-aws-ciris"))
  .dependsOn(`fs2-aws`)
  .settings(
    name := "fs2-aws-ciris",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest"               % V.ScalaTest % Test,
      "org.mockito"   % "mockito-core"             % V.MockitoCore % Test,
      "org.mockito"   %% "mockito-scala-scalatest" % V.MockitoScalaTest % Test,
      "is.cir"        %% "ciris"                   % "1.2.1"
    ),
    coverageMinimum       := 40,
    coverageFailOnMinimum := true
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-dynamodb` = (project in file("fs2-aws-dynamodb"))
  .dependsOn(`fs2-aws-core`)
  .settings(
    name                  := "fs2-aws-dynamodb",
    coverageMinimum       := 40,
    coverageFailOnMinimum := true,
    libraryDependencies ++= Seq(
      "co.fs2"        %% "fs2-core"                        % V.Fs2,
      "co.fs2"        %% "fs2-io"                          % V.Fs2,
      "org.scalatest" %% "scalatest"                       % V.ScalaTest % Test,
      "org.mockito"   % "mockito-core"                     % V.MockitoCore % Test,
      "org.mockito"   %% "mockito-scala-scalatest"         % V.MockitoScalaTest % Test,
      "com.amazonaws" % "dynamodb-streams-kinesis-adapter" % "1.5.2",
      "io.laserdisc"  %% "scanamo-circe"                   % "1.0.8"
    )
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-examples` = (project in file("fs2-aws-examples"))
  .dependsOn(`fs2-aws-dynamodb`)
  .settings(
    name            := "fs2-aws-examples",
    coverageMinimum := 0,
    libraryDependencies ++= Seq(
      "org.mockito"       % "mockito-core"             % V.MockitoCore % Test,
      "org.mockito"       %% "mockito-scala-scalatest" % V.MockitoScalaTest % Test,
      "ch.qos.logback"    % "logback-classic"          % "1.2.3",
      "ch.qos.logback"    % "logback-core"             % "1.2.3",
      "org.slf4j"         % "jcl-over-slf4j"           % "1.7.30",
      "org.slf4j"         % "jul-to-slf4j"             % "1.7.30",
      "io.chrisdavenport" %% "log4cats-slf4j"          % "1.1.1",
      "io.laserdisc"      %% "scanamo-circe"           % "1.0.8"
    )
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))
  .settings(
    skip in publish := true
  )

lazy val `fs2-aws-s3` = (project in file("fs2-aws-s3"))
  .settings(
    name := "fs2-aws-s3",
    libraryDependencies ++= Seq(
      "co.fs2"                 %% "fs2-core" % V.Fs2,
      "co.fs2"                 %% "fs2-io"   % V.Fs2,
      "eu.timepit"             %% "refined"  % V.Refined,
      "software.amazon.awssdk" % "s3"        % V.AwsSdkS3,
      "org.scalameta"          %% "munit"    % V.Munit % Test
    ),
    testFrameworks        += new TestFramework("munit.Framework"),
    coverageMinimum       := 0,
    coverageFailOnMinimum := true
  )
  .settings(commonSettings)
  .settings(scalacOptions := commonOptions(scalaVersion.value))

lazy val `fs2-aws` = (project in file("fs2-aws"))
  .dependsOn(`fs2-aws-core`)
  .settings(
    name := "fs2-aws",
    libraryDependencies ++= Seq(
      "co.fs2"                  %% "fs2-core"                % V.Fs2,
      "co.fs2"                  %% "fs2-io"                  % V.Fs2,
      "com.amazonaws"           % "aws-java-sdk-kinesis"     % V.AwsSdk,
      "com.amazonaws"           % "aws-java-sdk-s3"          % V.AwsSdk,
      "com.amazonaws"           % "amazon-kinesis-producer"  % "0.14.1",
      "software.amazon.kinesis" % "amazon-kinesis-client"    % "2.3.2",
      "org.mockito"             % "mockito-core"             % V.MockitoCore % Test,
      "software.amazon.awssdk"  % "sts"                      % "2.15.35",
      "org.scalatest"           %% "scalatest"               % V.ScalaTest % Test,
      "org.mockito"             %% "mockito-scala-scalatest" % V.MockitoScalaTest % Test,
      "eu.timepit"              %% "refined"                 % V.Refined
    ),
    coverageMinimum       := 40,
    coverageFailOnMinimum := true
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-sqs` = (project in file("fs2-aws-sqs"))
  .settings(
    name := "fs2-aws-sqs",
    libraryDependencies ++= Seq(
      "co.fs2"        %% "fs2-core"                     % V.Fs2,
      "co.fs2"        %% "fs2-io"                       % V.Fs2,
      "com.amazonaws" % "aws-java-sdk-sqs"              % V.AwsSdk excludeAll ("commons-logging", "commons-logging"),
      "com.amazonaws" % "amazon-sqs-java-messaging-lib" % "1.0.8" excludeAll ("commons-logging", "commons-logging"),
      "org.mockito"   % "mockito-core"                  % V.MockitoCore % Test,
      "org.scalatest" %% "scalatest"                    % V.ScalaTest % Test,
      "org.mockito"   %% "mockito-scala-scalatest"      % V.MockitoScalaTest % Test,
      "eu.timepit"    %% "refined"                      % V.Refined
    ),
    coverageMinimum       := 55.80,
    coverageFailOnMinimum := true
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-testkit` = (project in file("fs2-aws-testkit"))
  .dependsOn(`fs2-aws`)
  .settings(
    name := "fs2-aws-testkit",
    libraryDependencies ++= Seq(
      "io.circe"      %% "circe-core"              % V.Circe,
      "io.circe"      %% "circe-generic"           % V.Circe,
      "io.circe"      %% "circe-generic-extras"    % V.Circe,
      "io.circe"      %% "circe-parser"            % V.Circe,
      "org.scalatest" %% "scalatest"               % V.ScalaTest,
      "org.mockito"   % "mockito-core"             % V.MockitoCore,
      "org.mockito"   %% "mockito-scala-scalatest" % V.MockitoScalaTest
    )
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

lazy val `fs2-aws-sqs-testkit` = (project in file("fs2-aws-sqs-testkit"))
  .dependsOn(`fs2-aws-sqs`)
  .settings(
    name := "fs2-aws-sqs-testkit",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest"               % V.ScalaTest,
      "org.mockito"   % "mockito-core"             % V.MockitoCore,
      "org.mockito"   %% "mockito-scala-scalatest" % V.MockitoScalaTest
    )
  )
  .settings(commonSettings)
  .settings(scalacOptions ++= commonOptions(scalaVersion.value))

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
addCommandAlias("format", ";scalafmt;test:scalafmt;scalafmtSbt")
addCommandAlias("checkFormat", ";scalafmtCheck;test:scalafmtCheck;scalafmtSbtCheck")
addCommandAlias("build", ";checkFormat;clean;+test;coverage")

def commonOptions(scalaVersion: String) =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 12)) =>
      Seq("-Ypartial-unification")
    case _ => Seq.empty
  }

lazy val commonSettings = Seq(
  organization := "io.laserdisc",
  developers := List(
    Developer(
      "semenodm",
      "Dmytro Semenov",
      "sdo.semenov@gmail.com",
      url("https://github.com/semenodm")
    )
  ),
  licenses           ++= Seq(("MIT", url("http://opensource.org/licenses/MIT"))),
  crossScalaVersions := supportedScalaVersions,
  scalaVersion       := scala213,
  fork               in Test := true,
  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",                         // source files are in UTF-8
    "-deprecation",                  // warn about use of deprecated APIs
    "-unchecked",                    // warn about unchecked type parameters
    "-feature",                      // warn about misused language features
    "-language:higherKinds",         // allow higher kinded types without `import scala.language.higherKinds`
    "-language:implicitConversions", // allow use of implicit conversions
    "-language:postfixOps",
    "-Xlint",             // enable handy linter warnings
    "-Xfatal-warnings",   // turn compiler warnings into errors
    "-Ywarn-macros:after" // allows the compiler to resolve implicit imports being flagged as unused
  ),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
  addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
  libraryDependencies += "org.scala-lang.modules" %% "scala-collection-compat" % "2.3.1"
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")
