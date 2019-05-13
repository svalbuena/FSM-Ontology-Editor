package infrastructure.jena

import java.io.FileOutputStream

import domain.fsm.FiniteStateMachine
import domain.repository.{FsmRepository, Properties}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{RDFDataMgr, RDFLanguages}

/**
  * Jena implementation of the FsmRepository
  */
class JenaFsmRepository extends FsmRepository {

  override def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _] = {
    //val FsmBaseUri = "file:///D:/projects/ontologies/siot/demo_siot#"

    val jenaWriter = new JenaWriter(properties, fsm.baseUri)
    val fsmModel = jenaWriter.writeFsm(fsm)

    val outputStream = new FileOutputStream(filename)

    println()

    RDFDataMgr.write(outputStream, fsmModel, RDFLanguages.TURTLE)

    Right(())
  }

  override def loadFsm(properties: Properties, filename: String): Either[Exception, FiniteStateMachine] = {
    val fsmModel = RDFDataMgr.loadModel(filename)
    val fsmSchema = RDFDataMgr.loadModel("D:\\projects\\ontologies\\fsm\\fsm.ttl")

    val fsmInferredModel = ModelFactory.createRDFSModel(fsmSchema, fsmModel)

    val jenaReader = new JenaReader(properties)

    jenaReader.readFsm(fsmInferredModel)
  }
}
