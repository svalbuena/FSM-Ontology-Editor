import infrastructure.jena.{JenaFsmRepository, JenaReader, JenaWriter, Properties}
import javafx.application.Application

object Main {
  def main(args: Array[String]): Unit = {
    val FsmBaseUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#"
    val FsmUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#siot_fsm"

    val FsmPrefix = "file:///D:/projects/ontologies/fsm/fsm.ttl#"
    val HttpPrefix = "http://www.w3.org/2011/http#"

    val filename = "D:\\\\projects\\\\ontologies\\\\siot\\\\demo_siot.ttl"

    val properties = new Properties(FsmPrefix, HttpPrefix)

    val fsmRepository = new JenaFsmRepository

    fsmRepository.loadFsm(FsmUri, properties, filename) match {
      case Left(error) => println(error.getMessage)
      case Right(fsmRead) =>
        val jenaWriter = new JenaWriter(properties, FsmBaseUri)
        val model = jenaWriter.writeFsm(fsmRead,"" )

        val jenaReader = new JenaReader(properties)
        val fsmOut = jenaReader.readFsm(model, FsmUri)

        println(s"Fsm -> ${fsmOut.name}")
        println("-- States --")
        for (state <- fsmOut.states) {
          println(s"\tState ${state.name}")
          println(s"\t\t${state.stateType}")
        }

        println("-- Transitions --")
        for (transition <- fsmOut.transitions) {
          println("\tTransition " + transition.name)
          println("\t\tSource ->" + transition.source.name)
          println("\t\tDestination ->" + transition.destination.name)
        }

    }



    //fsmRepo.loadFsm("D:\\projects\\ontologies\\siot\\demo_siot.ttl")
    //Application.launch(classOf[MainApplication])
  }
}
