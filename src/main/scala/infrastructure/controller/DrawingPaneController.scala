package infrastructure.controller

import application.command.fsm.add.AddFsmCommand
import application.command.fsm.modify.SelectFsmCommand
import application.commandhandler.fsm.add.AddFsmHandler
import application.commandhandler.fsm.modify.SelectFsmHandler
import infrastructure.controller.action.ActionController
import infrastructure.controller.end.EndController
import infrastructure.controller.start.StartController
import infrastructure.controller.state.StateController
import infrastructure.controller.transition.TransitionController
import infrastructure.drawingpane.{Canvas, DrawingPane, MousePosition}
import infrastructure.element.ConnectableElement
import infrastructure.element.action.ActionType
import infrastructure.element.end.End
import infrastructure.element.ghostnode.GhostNode
import infrastructure.element.start.Start
import infrastructure.element.state.State
import infrastructure.element.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.input.{MouseButton, MouseEvent}

class DrawingPaneController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBox) {
  private val mousePosition = new MousePosition()

  private var tempTransitionOption: Option[Transition] = None
  private var ghostNodeOption: Option[GhostNode] = None

  val canvas: Canvas = drawingPane.canvas

  private val idGenerator = new IdGenerator
  private val addFsmHandler = new AddFsmHandler
  private val selectFsmHandler = new SelectFsmHandler

  addFsmHandler.execute(new AddFsmCommand) match {
    case Left(error) => println(error.getMessage)
    case Right(fsmName) => selectFsmHandler.execute(new SelectFsmCommand(fsmName)) match {
      case Left(error) => println(error.getMessage)
      case Right(_) =>
    }
  }


  val state1 = StateController.addStateToFsm(0, 0, this)
  val state2 = StateController.addStateToFsm(200, 200, this)

  val action1 = ActionController.addActionToState(ActionType.ENTRY, state1.get, this)
  val action2 = ActionController.addActionToState(ActionType.EXIT, state1.get, this)

  val transition1 = TransitionController.addStateToStateTransitionToFsm(state1.get, state2.get, this)

  drawingPane.setOnMouseClicked(drawingPaneMouseClickedListener)
  drawingPane.setOnMouseMoved(drawingPaneMouseMovedListener)

  def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    updateMousePosition(event)

    if (event.getButton == MouseButton.PRIMARY) {
      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          if (tempTransitionOption.isDefined) {
            cancelTransitionCreation()
            toolBox.setToolToDefault()
          }
        case _: StateItem =>
          StateController.addStateToFsm(event.getX, event.getY, this)
          toolBox.setToolToDefault()
        case _: StartItem =>
          StartController.addStart(event.getX, event.getY, this)
          toolBox.setToolToDefault()
        case _: EndItem =>
          EndController.addEnd(event.getX, event.getY, this)
          toolBox.setToolToDefault()
        case _ =>
      }
    }
  }

  def drawingPaneMouseMovedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        if (isTemporalTransitionDefined) {
          val tempTransition = tempTransitionOption.get
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          canvas.moveConnectableNode(ghostNodeOption.get.shape, deltaX, deltaY)
          canvas.moveTransition(tempTransition.shape, tempTransition.getSourceShape, tempTransition.getDestinationShape)
        }
      case _ =>
    }

    updateMousePosition(event)
  }

  def transitionToolUsed(connectableElement: ConnectableElement, point: Point2D): Unit = {
    if (isTemporalTransitionDefined) {
      if (establishTemporalTransition(connectableElement)) {
        toolBox.setToolToDefault()
      }
    } else {
      drawTemporalTransition(connectableElement, point.getX, point.getY)
    }
  }

  def cancelTransitionCreation(): Unit = {
    canvas.getChildren.remove(tempTransitionOption.get.shape)
    canvas.getChildren.remove(ghostNodeOption.get.shape)

    tempTransitionOption = None
    ghostNodeOption = None
  }

  def updateMousePosition(event: MouseEvent): Unit = {
    mousePosition.x = event.getSceneX
    mousePosition.y = event.getSceneY
  }

  def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mousePosition.x, event.getSceneY - mousePosition.y)
  }

  def isTemporalTransitionDefined: Boolean = tempTransitionOption.isDefined


  private def drawTemporalTransition(source: ConnectableElement, x: Double, y: Double): Unit = {
    ghostNodeOption = Some(new GhostNode("ghostElement"))
    ghostNodeOption.get.shape.setPrefSize(1, 1)

    canvas.drawConnectableNode(ghostNodeOption.get.shape, x, y)

    val transition = new Transition("tempTransition", source, ghostNodeOption.get)
    TransitionController.drawTransition(transition, this)

    ghostNodeOption.get.addInTransition(transition)

    tempTransitionOption = Some(transition)
  }

  private def establishTemporalTransition(destination: ConnectableElement): Boolean = {
    val source = tempTransitionOption.get.source
    var established = true

    (source, destination) match {
      case (srcState: State, dstState: State) =>
        TransitionController.addStateToStateTransitionToFsm(srcState, dstState, this)

      case (start: Start, state: State) =>
        TransitionController.addStartToStateTransition(start, state, this)

      case (state: State, end: End) =>
        TransitionController.addStateToEndTransition(state, end, this)

      case _ =>
        established = false
    }

    if (established) {
      canvas.getChildren.remove(tempTransitionOption.get.shape)
      tempTransitionOption = None
    }

    established
  }
}
