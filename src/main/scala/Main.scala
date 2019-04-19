import infrastructure.JenaFsmRepository
import javafx.application.Application

object Main {
  def main(args: Array[String]): Unit = {
    val fsmRepo = new JenaFsmRepository
    fsmRepo.loadFsm("files/demo_siot.ttl")
    //Application.launch(classOf[MainApplication])
  }
}
