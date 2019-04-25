import infrastructure.MainApplication
import infrastructure.jena.{JenaFsmRepository, Properties}
import javafx.application.Application

object Main {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[MainApplication])
  }
}
