package infrastructure.drawingpane

import infrastructure.drawingpane.shape.{End, Start, State, Transition}
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.NormalMouseSelector
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class DrawingPane(toolBox: ToolBox) extends Pane {
  val DefaultStateWidth = 200.0
  val DefaultStateHeight = 150.0
  val DefaultStartRadius = 20.0
  val DefaultEndRadius = 20.0

  var mouseX: Double = 0.0
  var mouseY: Double = 0.0
  var posX: Double = 0.0
  var posY: Double = 0.0

  var newTransitionNode: Option[Node] = None
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
              nodePressedWithTransitionTool(state)
            case start: Start =>
              nodePressedWithTransitionTool(start)
            case end: End =>
              nodePressedWithTransitionTool(end)
            case _ =>
          }
        case _: StateItem =>
          mousePressedWithStateItem(event)
        case _: EndItem =>
          mousePressedWithEndTool(event)
        case _: StartItem =>
          mousePressedWithStartItem(event)
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
          case start: Start =>
            startDragged(event, start)
          case end: End =>
            endDragged(event, end)
          case _ =>
        }
      case _ =>
    }

    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def mousePressedWithStartItem(event: MouseEvent): Unit = {
    val start = new Start(DefaultStartRadius)
    start.setTranslateX(event.getX)
    start.setTranslateY(event.getY)

    drawNode(start)

    toolBox.setToolToDefault()
  }

  private def mousePressedWithEndTool(event: MouseEvent): Unit = {
    val end = new End(DefaultEndRadius)
    end.setTranslateX(event.getX)
    end.setTranslateY(event.getY)
    println("Pressed")

    drawNode(end)

    toolBox.setToolToDefault()
  }

  private def mousePressedWithStateItem(event: MouseEvent): Unit = {
    val state = new State(DefaultStateWidth, DefaultStateHeight)
    state.setTranslateX(event.getX)
    state.setTranslateY(event.getY)

    drawNode(state)

    toolBox.setToolToDefault()
  }

  private def mouseMovedWithTransitionTool(event: MouseEvent): Unit = {
    if (newTransitionNode.isDefined) {
      val nodeBounds = newTransitionNode.get.getBoundsInParent
      val (startX, startY) = (nodeBounds.getCenterX, nodeBounds.getCenterY)
      newTransitionLine.setStartX(startX)
      newTransitionLine.setStartY(startY)
      newTransitionLine.setEndX(event.getX)
      newTransitionLine.setEndY(event.getY)
      newTransitionLine.setVisible(true)
    }
  }

  private def nodePressedWithTransitionTool(node: Node): Option[Transition] = {
    var transition: Option[Transition] = None

    if (newTransitionNode.isEmpty) {
      newTransitionNode = Some(node)
    } else {
      transition = Some(new Transition(newTransitionNode.get, node))

      node match {
        case state: State => state.addTransition(transition.get)
        case start: Start => start.addTransition(transition.get)
        case end: End => end.addTransition(transition.get)
      }

      newTransitionNode.get match {
        case state: State => state.addTransition(transition.get)
        case start: Start => start.addTransition(transition.get)
        case end: End => end.addTransition(transition.get)
      }

      drawNode(transition.get)
      transition.get.toBack()

      newTransitionNode = None
      newTransitionLine.setVisible(false)
      toolBox.setToolToDefault()
    }

    transition
  }

  private def stateDragged(event: MouseEvent, state: State): Unit = {
    if (moveNode(event, state)) {
      state.transitionList.foreach(transition => transition.redraw())
    }
  }

  private def startDragged(event: MouseEvent, start: Start): Unit = {
    if (moveNode(event, start)) {
      start.transitionList.foreach(transition => transition.redraw())
    }
  }

  private def endDragged(event: MouseEvent, end: End): Unit = {
    if (moveNode(event, end)) {
      println("End dragged")
      end.transitionList.foreach(transition => transition.redraw())
    }
  }

  private def moveNode(event: MouseEvent, node: Node): Boolean = {
    val deltaX = event.getSceneX - mouseX
    val deltaY = event.getSceneY - mouseY

    val shapeBounds = node.getBoundsInParent

    val shapeX = node.getTranslateX
    val shapeY = node.getTranslateY

    val newX = shapeX + deltaX
    val newY = shapeY + deltaY

    var moved = false
    if (node.getParent.getLayoutBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
      node.setTranslateX(newX)
      node.setTranslateY(newY)
      moved = true
    }

    moved
  }
}
