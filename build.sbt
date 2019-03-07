name := "fsm_ontology_editor"

version := "0.1"

scalaVersion := "2.12.8"

val osName: SettingKey[String] = SettingKey[String]("osName")

osName := (System.getProperty("os.name") match {
  case osFullName if osFullName.startsWith("Linux") => "linux"
  case osFullName if osFullName.startsWith("Mac") => "mac"
  case osFullName if osFullName.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
})

libraryDependencies ++= Seq (
  "org.openjfx" % "javafx-controls" % "11.0.2" classifier osName.value withSources() withJavadoc(),
  "org.openjfx" % "javafx-base" % "11.0.2" classifier osName.value withSources() withJavadoc(),
  "org.openjfx" % "javafx-graphics" % "11.0.2" classifier osName.value withSources() withJavadoc()
)

