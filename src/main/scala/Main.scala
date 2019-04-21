import infrastructure.jena.{JenaFsmRepository, Properties}
import javafx.application.Application

object Main {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[MainApplication])

    /*val FsmBaseUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#"
    val FsmUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#siot_fsm"

    val FsmPrefix = "file:///D:/projects/ontologies/fsm/fsm.ttl#"
    val HttpPrefix = "http://www.w3.org/2011/http#"
    val HttpMethodsPrefix = "http://www.w3.org/2011/http-methods#"

    val filename = "D:\projects\ontologies\siot\demo_siot.ttl"

    val properties = new Properties(FsmPrefix, HttpPrefix, HttpMethodsPrefix)

    val fsmRepository = new JenaFsmRepository

    fsmRepository.loadFsm(fsmUri = FsmUri, properties, filename) match {
      case Left(value) =>
      case Right(fsm) =>
        fsmRepository.saveFsm(fsm, properties, "files/demo_siot_test.ttl")
    }*/
  }
}
