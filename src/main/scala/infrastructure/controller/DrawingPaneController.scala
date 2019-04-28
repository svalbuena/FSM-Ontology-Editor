package infrastructure.controller

import infrastructure.drawingpane.shape.transition.TransitionShape
import infrastructure.drawingpane.{Canvas, DrawingPane, MousePosition}
import infrastructure.element.ConnectableElement
import infrastructure.element.end.End
import infrastructure.element.fsm.FiniteStateMachine
import infrastructure.element.ghostnode.GhostNode
import infrastructure.element.start.Start
import infrastructure.element.state.State
import infrastructure.element.transition.Transition
import infrastructure.propertybox.PropertiesBoxBar
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import javafx.event.EventHandler
import javafx.geometry.Point2D
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane

class DrawingPaneController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBoxBar) {
  private val mousePosition = new MousePosition()

  private var tempTransitionOption: Option[Transition] = None
  private var ghostNodeOption: Option[GhostNode] = None

  private var fsmOption: Option[FiniteStateMachine] = None

  private val canvas: Canvas = drawingPane.canvas
  canvas.setOnMouseClicked(drawingPaneMouseClickedListener)
  canvas.setOnMouseMoved(drawingPaneMouseMovedListener)


  def setFsm(fsm: FiniteStateMachine): Unit = {
    canvas.getChildren.clear()

    fsmOption = Some(fsm)

    FsmController.drawFiniteStateMachine(fsm, this)
  }

  private def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    if (fsmOption.isDefined) {
      updateMousePosition(event)

      if (event.getButton == MouseButton.PRIMARY) {
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            if (tempTransitionOption.isDefined) {
              cancelTransitionCreation()
              toolBox.setToolToDefault()
            }
          case _: StateItem =>
            StateController.addStateToFsm(event.getX, event.getY, fsmOption.get, this)
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
    } else {
      println("FSM is not selected!")
    }
  }

  private def drawingPaneMouseMovedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        if (isTemporalTransitionDefined) {
          val tempTransition = tempTransitionOption.get
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          canvas.moveNode(ghostNodeOption.get.shape, deltaX, deltaY)
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

  def removeNode(node: Node): Unit =  canvas.getChildren.remove(node)
  def drawNode(node: Node, x: Double, y: Double): Unit = canvas.drawNode(node, x, y)
  def moveNode(node: Node, deltaX: Double, deltaY: Double): Point2D = canvas.moveNode(node, deltaX, deltaY)
  def moveTransition(transition: TransitionShape, source: Node, destination: Node): Unit = canvas.moveTransition(transition, source, destination)
  def drawTransition(transition: TransitionShape, source: Node, destination: Node): Unit = canvas.drawTransition(transition, source, destination)

  private def isTemporalTransitionDefined: Boolean = tempTransitionOption.isDefined

  private def drawTemporalTransition(source: ConnectableElement, x: Double, y: Double): Unit = {
    if (fsmOption.isDefined) {
      ghostNodeOption = Some(new GhostNode("ghostElement"))
      ghostNodeOption.get.shape.setPrefSize(1, 1)

      canvas.drawNode(ghostNodeOption.get.shape, x, y)

      val transition = new Transition("tempTransition", source, ghostNodeOption.get, parent = fsmOption.get)
      TransitionController.drawTransition(transition, this)

      ghostNodeOption.get.addInTransition(transition)

      tempTransitionOption = Some(transition)
    } else {
      println("FSM not defined")
    }
  }

  private def establishTemporalTransition(destination: ConnectableElement): Boolean = {
    var established = true

    if (fsmOption.isDefined) {
      val source = tempTransitionOption.get.source

      (source, destination) match {
        case (srcState: State, dstState: State) =>
          TransitionController.addStateToStateTransitionToFsm(srcState, dstState, fsmOption.get, this)

        case (start: Start, state: State) =>
          TransitionController.addStartToStateTransition(start, state, fsmOption.get, this)

        case (state: State, end: End) =>
          TransitionController.addStateToEndTransition(state, end, fsmOption.get, this)

        case _ =>
          established = false
      }

      if (established) {
        canvas.getChildren.remove(tempTransitionOption.get.shape)
        tempTransitionOption = None
      }

    } else {
      established = false
      println("FSM is not selected")
    }

    established
  }
}
