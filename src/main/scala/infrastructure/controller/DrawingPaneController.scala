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
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.{Node, Scene}

/**
  * Controls the visual and behavior aspects of the drawing pane
  *
  * @param drawingPane   drawing pane to control
  * @param toolBox       toolbox of the drawing pane
  * @param propertiesBox propertiesbox of the drawing pane
  * @param scene         scene of the application
  */
class DrawingPaneController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBoxBar, scene: Scene) {
  private val mousePosition = new MousePosition()
  /*private var isCtrlPressed = false
  private val zoomScale = 1.1*/
  private val canvas: Canvas = drawingPane.canvas
  private var tempTransitionOption: Option[Transition] = None
  private var ghostNodeOption: Option[GhostNode] = None
  private var fsmOption: Option[FiniteStateMachine] = None
  canvas.setOnMouseClicked(canvasMouseClickedListener)
  canvas.setOnMouseMoved(canvasMouseMovedListener)

  /*scene.setOnKeyPressed(canvasKeyPressedListener)
  scene.setOnKeyReleased(canvasKeyReleasedListener)
  canvas.setOnMouseExited(canvasMouseExitedListener)
  canvas.setOnScroll(canvasScrollListener)*/

  /**
    * Sets the fsm to use and draw
    *
    * @param fsm fsm to be used
    */
  def setFsm(fsm: FiniteStateMachine): Unit = {
    canvas.getChildren.clear()

    fsmOption = Some(fsm)

    FsmController.drawFiniteStateMachine(fsm, this)
  }

  /**
    * Function to be called when the transition tool is clicked on an element
    *
    * @param connectableElement connectable element where the transition tool has been used
    * @param point              where the click has been done
    */
  def transitionToolUsed(connectableElement: ConnectableElement, point: Point2D): Unit = {
    if (isTemporalTransitionDefined) {
      if (establishTemporalTransition(connectableElement)) {
        toolBox.setToolToDefault()
      }
    } else {
      drawTemporalTransition(connectableElement, point.getX, point.getY)
    }
  }

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

  /**
    * Removes a node from the canvas
    *
    * @param node node to be removed
    */
  def removeNode(node: Node): Unit = canvas.getChildren.remove(node)

  /**
    * Draws a node on the canvas
    *
    * @param node node to be drawn
    * @param x    x coordinate of the node position
    * @param y    y coordinate of the node position
    */
  def drawNode(node: Node, x: Double, y: Double): Unit = canvas.drawNode(node, x, y)

  /**
    * Moves a node on the canvas
    *
    * @param node   node to be moved
    * @param deltaX deltaX of the move
    * @param deltaY deltaY of the move
    * @return true if the node could me move, false if the node wasn't moved because it would be out of bounds
    */
  def moveNode(node: Node, deltaX: Double, deltaY: Double): Point2D = canvas.moveNode(node, deltaX, deltaY)

  /**
    * Moves a transition on the canvas
    *
    * @param transition  transition shape to be moved
    * @param source      source shape of the transition
    * @param destination destination shape of the transition
    */
  def moveTransition(transition: TransitionShape, source: Node, destination: Node): Unit = canvas.moveTransition(transition, source, destination)

  /**
    * Draws a transition on the canvas
    *
    * @param transition  transition shape to be drawn
    * @param source      source shape of the transition
    * @param destination destination shape of the transition
    */
  def drawTransition(transition: TransitionShape, source: Node, destination: Node): Unit = canvas.drawTransition(transition, source, destination)

  private def canvasMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
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

  /**
    * Cancels the current temporal transition
    */
  def cancelTransitionCreation(): Unit = {
    canvas.getChildren.remove(tempTransitionOption.get.shape)
    canvas.getChildren.remove(ghostNodeOption.get.shape)

    tempTransitionOption = None
    ghostNodeOption = None
  }

  private def canvasMouseMovedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
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

  private def isTemporalTransitionDefined: Boolean = tempTransitionOption.isDefined

  /*private def canvasMouseExitedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
  if (fsmOption.isDefined) {
    isCtrlPressed = false
  }
}

private def canvasKeyPressedListener: EventHandler[KeyEvent] = (event: KeyEvent) => {
  if (fsmOption.isDefined) {
    if (event.getCode == KeyCode.CONTROL) {
      isCtrlPressed = true
    }
  }
}

private def canvasKeyReleasedListener: EventHandler[KeyEvent] = (event: KeyEvent) => {
  if (fsmOption.isDefined) {
    if (event.getCode == KeyCode.CONTROL) {
      isCtrlPressed = false
    }
  }
}

private def canvasScrollListener: EventHandler[ScrollEvent] = (event: ScrollEvent) => {
  if (fsmOption.isDefined) {
    if (isCtrlPressed) {
      event.consume()

      val deltaY = event.getDeltaY
      val scaleFactor = {
        if (deltaY == 0) 1
        else if (deltaY > 0) zoomScale
        else 1 / zoomScale
      }

      canvas.setScaleX(canvas.getScaleX * scaleFactor)
      canvas.setScaleY(canvas.getScaleY * scaleFactor)
    }
  }
}*/

  /**
    * Updates the mouse position data
    *
    * @param event mouse event where the data is extracted
    */
  def updateMousePosition(event: MouseEvent): Unit = {
    mousePosition.x = event.getSceneX
    mousePosition.y = event.getSceneY
  }

  /**
    * Calculates the delta from the previous mouse position and the new one
    *
    * @param event the new mouse event with the current position
    * @return the deltaX and deltaY
    */
  def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mousePosition.x, event.getSceneY - mousePosition.y)
  }
}
