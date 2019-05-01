package infrastructure.controller

import infrastructure.DomainToInfrastructureConverter
import infrastructure.drawingpane.DrawingPane
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.filechooser.FsmFileChooser
import infrastructure.propertybox.PropertiesBoxBar
import infrastructure.toolbar.item.FileMenu
import infrastructure.toolbox.ToolBox
import javafx.scene.Scene
import javafx.stage.Stage

class MainController(scene: Scene, stage: Stage, drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBoxBar, fileMenu: FileMenu) {

  //Create the DrawingPaneController
  private val drawingPaneController = new DrawingPaneController(drawingPane, toolBox, propertiesBox, scene)
  //Create the FileMenuController
  private val fileMenuController = new FileMenuController(fileMenu, this)

  private val fsmFileChooser = new FsmFileChooser(stage)
  private var fsmList: List[FiniteStateMachine] = List()
  private var selectedFsmOption: Option[FiniteStateMachine] = None

  newFsm()
  //loadFsm("D:\\projects\\ontologies\\siot\\demo_siot.ttl")

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

  def newFsm(): Unit = {
    FsmController.addFsm() match {
      case Left(error) => println(error.getMessage)
      case Right((fsmName, fsmBaseUri)) =>
        val fsm = new FiniteStateMachine(fsmName, fsmBaseUri)
        fsmList = fsm :: fsmList

        fileMenuController.setSaveButtonDisable(disable = true)

        selectFsm(fsm)
    }
  }

  def loadFsm(): Unit = {
    fsmFileChooser.askForFileToOpen() match {
      case Some(filename) =>
        //val filename = "D:\\projects\\ontologies\\siot\\demo_siot.ttl"
        println(filename)
        FsmController.loadFsm(filename) match {
          case Left(error) => println(error.getMessage)
          case Right(domainFsm) =>
            println("Converting")
            val fsm = DomainToInfrastructureConverter.convertFsm(domainFsm)

            fsmList = fsm :: fsmList
            selectFsm(fsm)
        }
      case None =>
    }
  }

  def selectFsm(fsm: FiniteStateMachine): Either[Exception, _] = {
    FsmController.selectFsm(fsm.name) match {
      case Left(error) =>
        println(error.getMessage)
        Left(error)
      case Right(_) =>
        selectedFsmOption = Some(fsm)
        drawingPaneController.setFsm(fsm)
        propertiesBox.setFsmPropertiesBox(fsm.propertiesBox)
        Right(())
    }
  }

  def saveAsFsm(): Unit = {
    if (selectedFsmOption.isDefined) {
      fsmFileChooser.askForFileToSave() match {
        case Some(filename) =>
          //val filename = "D:\\projects\\ontologies\\siot\\demo_siot.ttl"
          val fsm = selectedFsmOption.get
          fsm.setFilename(filename)
          saveFsm()
        case None =>
      }
    }
  }

  def saveFsm(): Unit = {
    if (selectedFsmOption.isDefined) {
      val fsm = selectedFsmOption.get
      if (fsm.isFilenameDefined) {
        val filename = fsm.getFilename

        FsmController.saveFsm(filename) match {
          case Left(error) => println(error.getMessage)
          case Right(_) =>
            println("Save successful")
            fileMenuController.setSaveButtonDisable(disable = false)
        }

      } else {
        println("Save error -> FSM filename not defined")
      }
    } else {
      println("Save error -> FSM not selected")
    }
  }
}
