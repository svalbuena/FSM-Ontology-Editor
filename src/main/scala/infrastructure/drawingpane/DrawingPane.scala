package infrastructure.drawingpane

import infrastructure.drawingpane.shape.{State, Transition}
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class DrawingPane(toolBox: ToolBox) extends Pane {
  val DefaultStateWidth = 200
  val DefaultStateHeight = 150

  var mouseX: Double = 0.0
  var mouseY: Double = 0.0
  var posX: Double = 0.0
  var posY: Double = 0.0

  var newTransitionState: Option[State] = None
  var newTransitionLine = new Line()
  newTransitionLine.setVisible(false)

  getChildren.add(newTransitionLine)

  val state1 = new State(DefaultStateWidth, DefaultStateHeight)
  val state2 = new State(DefaultStateWidth, DefaultStateHeight)

  state2.setTranslateX(300)

  drawNode(state1)
  drawNode(state2)

  setOnMousePressed(mousePressed())
  setOnMouseDragged(mouseDragged())
  setOnMouseMoved(mouseMoved())

  def drawNode(shape: Node): Unit = {
    shape.setOnMousePressed(mousePressed())
    shape.setOnMouseDragged(mouseDragged())
    getChildren.add(shape)
  }

  private def mouseMoved(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        mouseMovedWithTransitionTool(event)
      case _ =>
    }
  }

  private def mousePressed(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    if (event.getButton == MouseButton.PRIMARY) {
      mouseX = event.getSceneX
      mouseY = event.getSceneY

      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          event.getSource match {
            case state: State =>
              statePressedWithTransitionTool(state)
            case _ =>
          }
        case _: StateItem =>
          mousePressedWithStateTool(event)
        case _ =>
      }
    } else if (event.getButton == MouseButton.SECONDARY) {
      event.getSource match {
        case node: Node =>
          node.setTranslateX(0)
          node.setTranslateY(0)
      }
    }
  }

  private def mouseDragged(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: NormalMouseSelector =>
        event.getSource match {
          case state: State =>
            stateDragged(event, state)
          case _ =>
        }
      case _ =>
    }

    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def mouseMovedWithTransitionTool(event: MouseEvent): Unit = {
    if (newTransitionState.isDefined) {
      val (startX, startY) = newTransitionState.get.getCenterCoordinates
      newTransitionLine.setStartX(startX)
      newTransitionLine.setStartY(startY)
      newTransitionLine.setEndX(event.getX)
      newTransitionLine.setEndY(event.getY)
      newTransitionLine.setVisible(true)
    }
  }

  private def statePressedWithTransitionTool(state: State): Unit = {
    if (newTransitionState.isEmpty) {
      newTransitionState = Some(state)
    } else {
      val transition = new Transition(newTransitionState.get, state)
      newTransitionState.get.addTransition(transition)
      state.addTransition(transition)
      drawNode(transition)
      transition.toBack()

      newTransitionState = None
      newTransitionLine.setVisible(false)
      toolBox.setToolToDefault()
    }
  }

  private def mousePressedWithStateTool(event: MouseEvent): Unit = {
    val state = new State(DefaultStateWidth, DefaultStateHeight)
    state.setTranslateX(event.getX)
    state.setTranslateY(event.getY)

    drawNode(state)

    toolBox.setToolToDefault()
  }

  private def stateDragged(event: MouseEvent, state: State): Unit = {
    val deltaX = event.getSceneX - mouseX
    val deltaY = event.getSceneY - mouseY

    val shapeBounds = state.getBoundsInParent

    val shapeX = state.getTranslateX
    val shapeY = state.getTranslateY

    val newX = shapeX + deltaX
    val newY = shapeY + deltaY

    if (state.getParent.getLayoutBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
      state.setTranslateX(newX)
      state.setTranslateY(newY)

      state.transitionList.foreach(transition => {
        val (state1X, state1Y) = transition.state1.getCenterCoordinates
        val (state2X, state2Y) = transition.state2.getCenterCoordinates

        transition.moveLine(state1X, state1Y, state2X, state2Y)
      })
    }
  }
}
