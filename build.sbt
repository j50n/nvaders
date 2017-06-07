(emitSourceMaps in fullOptJS) := false

enablePlugins(ScalaJSPlugin)

name := "nvaders"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.2"

scalaJSUseMainModuleInitializer := true

isSnapshot := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "com.lihaoyi" %%% "scalatags" % "0.6.5",
  "org.scala-js" %%% "scalajs-java-time" % "0.2.1")

        