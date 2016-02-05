name := """awsscala"""
organization := "com.taxis99"

val awsVersion = "1.10.50"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

enablePlugins(GitVersioning)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.10.5", "2.11.7")

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-sqs" % awsVersion,
  "com.amazonaws" % "aws-java-sdk-sns" % awsVersion,
  "com.amazonaws" % "aws-java-sdk-s3" % awsVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)

bintraySettings
