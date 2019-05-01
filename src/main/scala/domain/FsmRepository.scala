package domain

import domain.fsm.FiniteStateMachine
import infrastructure.jena.Properties

/**
  * Repository for storing and loading an fsm annotated with ontologies from files
  */
trait FsmRepository {
  /**
    *
    * @param fsm        fsm instance to store
    * @param properties classes and prototypes of the ontologies to use to store the fsm
    * @param filename   path to the file to store the fsm
    * @return
    */
  def saveFsm(fsm: FiniteStateMachine, properties: Properties, filename: String): Either[Exception, _]

  /**
    *
    * @param properties classes and prototypes of the ontologies to use to load the fsm
    * @param filename   path to the file to load the fsm
    * @return an fsm instance with the data loaded
    */
  def loadFsm(properties: Properties, filename: String): Either[Exception, FiniteStateMachine]
}
