package infrastructure.controller

import infrastructure.DomainToInfrastructureConverter
import infrastructure.controller.action.ActionController
import infrastructure.controller.fsm.FsmController
import infrastructure.controller.state.StateController
import infrastructure.controller.toolbar.FileMenuController
import infrastructure.controller.transition.TransitionController
import infrastructure.drawingpane.DrawingPane
import infrastructure.element.action.ActionType
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbar.item.FileMenu
import infrastructure.toolbox.ToolBox

class MainController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBox, fileMenu: FileMenu) {
  private var fsmList: List[FiniteStateMachine] = List()
  private var selectedFsmOption: Option[FiniteStateMachine] = None

  //Create the DrawingPaneController
  private val drawingPaneController = new DrawingPaneController(drawingPane, toolBox, propertiesBox)

  //Create the FileMenuController
  private val FileMenuController = new FileMenuController(fileMenu)

  loadFsm("D:\\projects\\ontologies\\siot\\demo_siot.ttl")

  /*newFsm() match {
    case Left(value) =>
    case Right(fsm) =>
      selectFsm(fsm) match {
        case Left(value) =>
        case Right(_) =>
          val state1 = StateController.addStateToFsm(0, 0, fsm, drawingPaneController)
          val state2 = StateController.addStateToFsm(200, 200, fsm, drawingPaneController)

          val action1 = ActionController.addActionToState(ActionType.ENTRY, state1.get, drawingPaneController)
          val action2 = ActionController.addActionToState(ActionType.EXIT, state1.get, drawingPaneController)

          val transition1 = TransitionController.addStateToStateTransitionToFsm(state1.get, state2.get, fsm, drawingPaneController)
      }
  }*/

  def newFsm(): Either[Exception, FiniteStateMachine] = {
    FsmController.addFsm() match {
      case Left(error) =>
        println(error.getMessage)
        Left(error)
      case Right(fsmName) =>
        val fsm = new FiniteStateMachine(fsmName)
        fsmList = fsm :: fsmList

        Right(fsm)
    }
  }

  def loadFsm(filename: String): Unit = {
    FsmController.loadFsm(filename) match {
      case Left(error) => println(error.getMessage)
      case Right(domainFsm) =>
        println("Converting")
        val fsm = DomainToInfrastructureConverter.convertFsm(domainFsm)

        fsmList = fsm :: fsmList
        selectFsm(fsm)
    }
  }

  def saveFsm(): Unit = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get
      if (fsm.isFilenameDefined) {
        val filename = fsm.getFilename
        saveAsFsm(filename)
      } else {
        println("Save error -> FSM filename not defined")
      }
    } else {
      println("Save error -> FSM not selected")
    }
  }

  def saveAsFsm(filename: String): Unit = {
    FsmController.saveFsm(filename) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
        println("Save successful")
    }
  }

  def selectFsm(fsm: FiniteStateMachine): Either[Exception, _] = {
    FsmController.selectFsm(fsm.name) match {
      case Left(error) =>
        println(error.getMessage)
        Left(error)
      case Right(_) =>
        drawingPaneController.setFsm(fsm)
        Right(())
    }
  }
}
