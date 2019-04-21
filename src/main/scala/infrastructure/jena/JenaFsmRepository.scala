package infrastructure.jena

import domain.FsmRepository
import domain.fsm.FiniteStateMachine
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.riot.RDFDataMgr

class JenaFsmRepository extends FsmRepository {

  override def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _] = {
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
