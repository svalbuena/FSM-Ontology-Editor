package infrastructure.controller

import application.command.fsm.add.{AddFsmCommand, LoadFsmCommand}
import application.command.fsm.modify.{ModifyFsmBaseUriCommand, ModifyFsmNameCommand}
import application.command.fsm.remove.RemoveFsmCommand
import application.command.fsm.save.SaveFsmCommand
import application.commandhandler.fsm.add.{AddFsmHandler, LoadFsmHandler}
import application.commandhandler.fsm.modify.{ModifyFsmBaseUriHandler, ModifyFsmNameHandler}
import application.commandhandler.fsm.remove.RemoveFsmHandler
import application.commandhandler.fsm.save.SaveFsmHandler
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.main.EnvironmentSingleton

/**
  * Controls the visual and behavior aspects of an fsm
  *
  * @param fsm fsm to control
  */
class FsmController(fsm: FiniteStateMachine) {
  private val propertiesBox = fsm.propertiesBox

  propertiesBox.setOnFsmNameChanged(newFsmName => FsmController.modifyFsmName(fsm, newFsmName))
  propertiesBox.setOnBaseUriChanged(newBaseUri => FsmController.modifyBaseUri(fsm, newBaseUri))
}

/**
  * Operations that can be done with an Fsm
  */
object FsmController {
  private val environment = EnvironmentSingleton.get()

  /**
    * Adds an fsm to the system
    *
    * @return an exception or the fsm name and the base uri
    */
  def addFsm(): Either[Exception, (String, String)] = new AddFsmHandler(environment).execute(new AddFsmCommand)

  /**
    * Removes an fsm from the system
    *
    * @param fsmName name of the fsm to be removed
    */
  def removeFsm(fsmName: String): Unit = new RemoveFsmHandler(environment).execute(new RemoveFsmCommand(fsmName))

  /**
    * Loads an fsm from a file
    *
    * @param filename file where the fsm data is stored
    * @return exception or the domain fsm instance
    */
  def loadFsm(filename: String): Either[Exception, domain.element.fsm.FiniteStateMachine] = new LoadFsmHandler(environment).execute(new LoadFsmCommand(filename))

  /**
    * Saves an fsm to a file
    *
    * @param filename path to the file
    * @return exception or nothing if successful
    */
  def saveFsm(filename: String): Either[Exception, _] = new SaveFsmHandler(environment).execute(new SaveFsmCommand(filename))

  /**
    * Modifies the name of an fsm
    *
    * @param fsm        fsm to be modified
    * @param newFsmName new fsm name
    */
  def modifyFsmName(fsm: FiniteStateMachine, newFsmName: String): Unit = {
    new ModifyFsmNameHandler(environment).execute(new ModifyFsmNameCommand(newFsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>

        fsm.name = newFsmName

        println("Fsm name changed to -> " + newFsmName)
    }
  }

  /**
    * Modifies the base uri of an fsm
    *
    * @param fsm        fsm to be modified
    * @param newBaseUri new base uri
    */
  def modifyBaseUri(fsm: FiniteStateMachine, newBaseUri: String): Unit = {
    new ModifyFsmBaseUriHandler(environment).execute(new ModifyFsmBaseUriCommand(newBaseUri)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        fsm.baseUri = newBaseUri
    }
  }

  /**
    * Draws an fsm on the canvas
    *
    * @param fsm                   fsm to be drawn
    * @param drawingPaneController controller of the drawing pane
    */
  def drawFiniteStateMachine(fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Unit = {
    fsm.propertiesBox.setFsmName(fsm.name)
    fsm.propertiesBox.setBaseUri(fsm.baseUri)

    for (state <- fsm.states) {
      StateController.drawState(state, drawingPaneController)
    }

    for (transition <- fsm.transitions) {
      TransitionController.drawTransition(transition, drawingPaneController)
    }

    new FsmController(fsm)
  }
}
