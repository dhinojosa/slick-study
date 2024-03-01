
name := "Slick Study"

version := "1.0-SNAPSHOT"

scalaVersion := "2.13.13"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "com.h2database" % "h2" % "1.4.196"
)
