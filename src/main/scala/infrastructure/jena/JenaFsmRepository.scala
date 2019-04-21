package infrastructure.jena

import java.io.FileOutputStream

import domain.FsmRepository
import domain.fsm.FiniteStateMachine
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{RDFDataMgr, RDFLanguages}

class JenaFsmRepository extends FsmRepository {

  override def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _] = {
    val FsmBaseUri = "file:///D:/projects/ontologies/siot/demo_siot.ttl#"

    val jenaWriter = new JenaWriter(properties, FsmBaseUri)
    val fsmModel = jenaWriter.writeFsm(fsm, filename)

    val outputStream = new FileOutputStream(filename)

    println()

    RDFDataMgr.write(outputStream, fsmModel, RDFLanguages.TURTLE)

    Right(())
  }

  override def loadFsm(fsmUri: String, properties: Properties,  filename: String): Either[Exception, FiniteStateMachine] = {
    val fsmModel = RDFDataMgr.loadModel(filename)
    val fsmSchema = RDFDataMgr.loadModel("D:\\projects\\ontologies\\fsm\\fsm.ttl")

    val fsmInferredModel = ModelFactory.createRDFSModel(fsmSchema, fsmModel)

    val jenaReader = new JenaReader(properties)

    val fsm = jenaReader.readFsm(fsmInferredModel, fsmUri)

    println(fsm.states.length)

    Right(fsm)
  }
}
