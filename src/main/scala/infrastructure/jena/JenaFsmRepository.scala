package infrastructure.jena

import java.io.FileOutputStream

import domain.element.fsm.FiniteStateMachine
import domain.repository.{FsmRepository, Properties}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.{RDFDataMgr, RDFLanguages}

/**
  * Jena implementation of the FsmRepository
  */
class JenaFsmRepository extends FsmRepository {

  override def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _] = {
    val jenaWriter = new JenaWriter(properties, fsm.baseUri)
    val fsmModel = jenaWriter.writeFsm(fsm)

    val outputStream = new FileOutputStream(filename)

    println()

    RDFDataMgr.write(outputStream, fsmModel, RDFLanguages.TURTLE)

    outputStream.close()

    Right(())
  }

  override def loadFsm(properties: Properties, filename: String): Either[Exception, FiniteStateMachine] = {
    try {
      val fsmModel = RDFDataMgr.loadModel(filename)
      val fsmSchema = RDFDataMgr.loadModel("D:\\projects\\ontologies\\fsm\\fsm.ttl")

      val fsmInferredModel = ModelFactory.createRDFSModel(fsmSchema, fsmModel)
      val jenaReader = new JenaReader(properties)

      val fsm = jenaReader.readFsm(fsmInferredModel)

      fsmModel.close()
      fsmSchema.close()

      fsm
    } catch {
      case e: Exception => Left(new Exception("The content of the file is not valid"))
    }
  }
}
