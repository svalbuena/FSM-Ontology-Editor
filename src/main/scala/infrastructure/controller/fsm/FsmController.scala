package infrastructure.controller.fsm

import application.command.fsm.add.{AddFsmCommand, LoadFsmCommand}
import application.command.fsm.modify.{ModifyFsmNameCommand, SelectFsmCommand}
import application.command.fsm.remove.RemoveFsmCommand
import application.command.fsm.save.SaveFsmCommand
import application.commandhandler.fsm.add.{AddFsmHandler, LoadFsmHandler}
import application.commandhandler.fsm.modify.{ModifyFsmNameHandler, SelectFsmHandler}
import application.commandhandler.fsm.remove.RemoveFsmHandler
import application.commandhandler.fsm.save.SaveFsmHandler
import infrastructure.controller.DrawingPaneController
import infrastructure.controller.state.StateController
import infrastructure.controller.transition.TransitionController
import infrastructure.element.fsm.FiniteStateMachine

class FsmController(fsm: FiniteStateMachine) {

}

object FsmController {
  def addFsm(): Either[Exception, String] = new AddFsmHandler().execute(new AddFsmCommand)
  def removeFsm(fsmName: String): Unit = new RemoveFsmHandler().execute(new RemoveFsmCommand(fsmName))
  def loadFsm(filename: String): Either[Exception, domain.fsm.FiniteStateMachine] = new LoadFsmHandler().execute(new LoadFsmCommand(filename))
  def saveFsm(filename: String): Either[Exception, _] = new SaveFsmHandler().execute(new SaveFsmCommand(filename))
  def selectFsm(fsmName: String): Either[Exception, _] = new SelectFsmHandler().execute(new SelectFsmCommand(fsmName))

  def modifyFsmName(fsm: FiniteStateMachine, newFsmName: String): Unit = {
    new ModifyFsmNameHandler().execute(new ModifyFsmNameCommand(newFsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        fsm.name = newFsmName
      //TODO: implement ModifyFsmName
    }
  }

  def drawFiniteStateMachine(fsm: FiniteStateMachine, drawingPaneController: DrawingPaneController): Unit = {
    for (state <- fsm.states) {
      StateController.drawState(state, drawingPaneController)
    }

    for (transition <- fsm.transitions) {
      TransitionController.drawTransition(transition, drawingPaneController)
    }

    new FsmController(fsm)
  }
}
