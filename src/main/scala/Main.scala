import infrastructure.JenaFsmRepository
import javafx.application.Application

object Main {
  def main(args: Array[String]): Unit = {
    val fsmRepo = new JenaFsmRepository
    fsmRepo.loadFsm("D:\\projects\\ontologies\\siot\\demo_siot.owl")
    //Application.launch(classOf[MainApplication])
  }
}
