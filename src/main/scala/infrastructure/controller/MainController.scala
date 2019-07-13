package infrastructure.controller

import infrastructure.drawingpane.DrawingPane
import infrastructure.element.DomainToInfrastructureConverter
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.filechooser.FsmFileChooser
import infrastructure.propertybox.PropertiesBoxBar
import infrastructure.toolbar.item.FileMenu
import infrastructure.toolbox.ToolBox
import javafx.scene.Scene
import javafx.stage.Stage

/**
  * Main controller of the application, coordinates the other elements
  *
  * @param scene         scene of the application
  * @param stage         stage of the application
  * @param drawingPane   drawing pane to use
  * @param toolBox       toolbox of the drawing pane
  * @param propertiesBox propertiesbox of the drawing pane
  * @param fileMenu      filemenu of the application
  */
class MainController(scene: Scene, stage: Stage, drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBoxBar, fileMenu: FileMenu) {

  //Create the DrawingPaneController
  private val drawingPaneController = new DrawingPaneController(drawingPane, toolBox, propertiesBox, scene)
  //Create the FileMenuController
  private val fileMenuController = new FileMenuController(fileMenu, this)

  private val fsmFileChooser = new FsmFileChooser(stage)
  private var fsmList: List[FiniteStateMachine] = List()
  private var selectedFsmOption: Option[FiniteStateMachine] = None

  newFsm()


  /**
    * Creates a new fsm and selects it
    */
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

  /**
    * Loads an fsm and selects it
    */
  def loadFsm(): Unit = {
    fsmFileChooser.askForFileToOpen().flatMap(filename => {
      FsmController.loadFsm(filename) match {
        case Left(error) =>
          println(error.getMessage)
          Some(false)

        case Right(domainFsm) =>
          val fsm = DomainToInfrastructureConverter.convertFsm(domainFsm)

          fsmList = fsm :: fsmList
          selectFsm(fsm)

          Some(true)
      }
    })
  }

  /**
    * Selects an fsm, error if it doesn't exist on the model
    *
    * @param fsm fsm to be selected
    * @return exception or nothing if successful
    */
  def selectFsm(fsm: FiniteStateMachine): Either[Exception, _] = {
      selectedFsmOption = Some(fsm)
      drawingPaneController.setFsm(fsm)
      propertiesBox.removeOtherPropertiesBoxContent()
      propertiesBox.setFsmPropertiesBox(fsm.propertiesBox)
      Right(())
  }

  /**
    * Saves an fsm asking the path
    */
  def saveAsFsm(): Unit = {
    if (selectedFsmOption.isDefined) {
      fsmFileChooser.askForFileToSave().flatMap(filename => {
        val fsm = selectedFsmOption.get
        fsm.setFilename(filename)
        saveFsm()

        Some(true)
      })
    }
  }

  /**
    * Saves an fsm with the actual name, if none it calls saveAsFsm()
    */
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
