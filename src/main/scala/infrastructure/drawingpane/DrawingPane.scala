package infrastructure.drawingpane

import infrastructure.drawingpane.shape._
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line

class DrawingPane(toolBox: ToolBox) extends Pane {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None

  val state1 = new State()
  val state2 = new State()

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
        mouseMovedWithTransitionItem(event)
      case _ =>
    }

    updateMousePosition(event)
  }

  private def mousePressed(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    if (event.getButton == MouseButton.PRIMARY) {
      updateMousePosition(event)

      toolBox.getSelectedTool match {
        case _: DeleteMouseSelector =>
          event.getSource match {
            case connectableNode: ConnectableNode =>
              connectableNodePressedWithTransitionItem(connectableNode)
            case _ =>
          }
        case _: TransitionItem =>
          event.getSource match {
            case connectableNode: ConnectableNode =>
              connectableNodePressedWithTransitionItem(connectableNode)
            case _ =>
          }
        case _: StateItem =>
          drawConnectableNode(new State(), mouseX, mouseY)
        case _: StartItem =>
          drawConnectableNode(new Start(), mouseX, mouseY)
        case _: EndItem =>
          drawConnectableNode(new End(), mouseX, mouseY)
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
          case connectableNode: ConnectableNode =>
            connectableNode.drag(event.getSceneX - mouseX, event.getSceneY - mouseY)
          case _ =>
        }
      case _ =>
    }

    updateMousePosition(event)
  }

  private def drawConnectableNode(connectableNode: ConnectableNode, x: Double, y: Double): Unit = {
    connectableNode.setTranslateX(x)
    connectableNode.setTranslateY(y)

    drawNode(connectableNode)

    toolBox.setToolToDefault()
  }


  private def mouseMovedWithTransitionItem(event: MouseEvent): Unit = {
    if (temporalTransition.isDefined) {
      val transition = temporalTransition.get
      val tempNode = transition.node2

      tempNode.drag(event.getSceneX - mouseX, event.getSceneY - mouseY)
      transition.redraw()
    }
  }

  private def connectableNodePressedWithTransitionItem(connectableNode: ConnectableNode): Unit = {
    if (temporalTransition.isEmpty) {
      val tempNode = new ConnectableNode
      tempNode.setTranslateX(connectableNode.getTranslateX)
      tempNode.setTranslateY(connectableNode.getTranslateY)
      drawNode(tempNode)

      val transition = new Transition(connectableNode, tempNode)
      drawNode(transition)

      temporalTransition = Some(transition)
    } else {
      val transition = temporalTransition.get
      val srcNode = transition.node1
      val dstNode = connectableNode

      transition.node2 = dstNode

      srcNode.addTransition(transition)
      dstNode.addTransition(transition)

      transition.redraw()
      transition.toBack()

      temporalTransition = None

      toolBox.setToolToDefault()
    }
  }

  private def updateMousePosition(event: MouseEvent): Unit = {
    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }
}
