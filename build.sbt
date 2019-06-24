name := "fsm_ontology_editor"

version := "0.1"

scalaVersion := "2.13.0"

val osName: SettingKey[String] = SettingKey[String]("osName")

osName := (System.getProperty("os.name") match {
  case osFullName if osFullName.startsWith("Linux") => "linux"
  case osFullName if osFullName.startsWith("Mac") => "mac"
  case osFullName if osFullName.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
})

libraryDependencies ++= Seq (
  "org.openjfx" % "javafx-controls" % "12.0.1" classifier osName.value withSources() withJavadoc(),
  "org.openjfx" % "javafx-base" % "12.0.1" classifier osName.value withSources() withJavadoc(),
  "org.openjfx" % "javafx-graphics" % "12.0.1" classifier osName.value withSources() withJavadoc(),
  "org.apache.jena" % "apache-jena-libs" % "3.12.0" pomOnly()
)

//scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

