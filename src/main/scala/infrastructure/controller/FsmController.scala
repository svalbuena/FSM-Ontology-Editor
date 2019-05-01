package infrastructure.controller

import application.command.fsm.add.{AddFsmCommand, LoadFsmCommand}
import application.command.fsm.modify.{ModifyFsmBaseUriCommand, ModifyFsmNameCommand, SelectFsmCommand}
import application.command.fsm.remove.RemoveFsmCommand
import application.command.fsm.save.SaveFsmCommand
import application.commandhandler.fsm.add.{AddFsmHandler, LoadFsmHandler}
import application.commandhandler.fsm.modify.{ModifyFsmBaseUriHandler, ModifyFsmNameHandler, SelectFsmHandler}
import application.commandhandler.fsm.remove.RemoveFsmHandler
import application.commandhandler.fsm.save.SaveFsmHandler
import infrastructure.element.fsm.FiniteStateMachine

class FsmController(fsm: FiniteStateMachine) {
  private val propertiesBox = fsm.propertiesBox

  propertiesBox.setOnFsmNameChanged(newFsmName => FsmController.modifyFsmName(fsm, newFsmName))
  propertiesBox.setOnBaseUriChanged(newBaseUri => FsmController.modifyBaseUri(fsm, newBaseUri))
}

object FsmController {
  def addFsm(): Either[Exception, (String, String)] = new AddFsmHandler().execute(new AddFsmCommand)

  def removeFsm(fsmName: String): Unit = new RemoveFsmHandler().execute(new RemoveFsmCommand(fsmName))

  def loadFsm(filename: String): Either[Exception, domain.fsm.FiniteStateMachine] = new LoadFsmHandler().execute(new LoadFsmCommand(filename))

  def saveFsm(filename: String): Either[Exception, _] = new SaveFsmHandler().execute(new SaveFsmCommand(filename))

  def selectFsm(fsmName: String): Either[Exception, _] = new SelectFsmHandler().execute(new SelectFsmCommand(fsmName))

  def modifyFsmName(fsm: FiniteStateMachine, newFsmName: String): Unit = {
    new ModifyFsmNameHandler().execute(new ModifyFsmNameCommand(newFsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        fsm.name = newFsmName
    }
  }

  def modifyBaseUri(fsm: FiniteStateMachine, newBaseUri: String): Unit = {
    new ModifyFsmBaseUriHandler().execute(new ModifyFsmBaseUriCommand(newBaseUri)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        fsm.baseUri = newBaseUri
    }
  }

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
