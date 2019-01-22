name := "akka-quickstart-java"

version := "1.0"

scalaVersion := "2.12.7"

lazy val akkaVersion = "2.5.19"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "mysql" % "mysql-connector-java" % "8.0.13",
  "org.postgresql" % "postgresql" % "42.2.5", 
  "junit" % "junit" % "4.12")
