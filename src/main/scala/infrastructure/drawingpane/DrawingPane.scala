package infrastructure.drawingpane

import infrastructure.drawingpane.shape._
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane

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

  private def mouseMoved(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    event.consume()
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        mouseMovedWithTransitionItem(event.getSceneX - mouseX, event.getSceneY - mouseY)
      case _ =>
    }

    updateMousePosition(event)
  }

  private def mousePressed(): EventHandler[MouseEvent] = (event: MouseEvent) => {
    event.consume()
    if (event.getButton == MouseButton.PRIMARY) {
      updateMousePosition(event)

      toolBox.getSelectedTool match {
        case _: DeleteMouseSelector =>
          event.getSource match {
            case connectableNode: ConnectableNode =>
              eraseConnectableNode(connectableNode)
            case _ =>
          }
        case _: TransitionItem =>
          event.getSource match {
            case connectableNode: ConnectableNode =>
              val point = connectableNode.getLocalToParentTransform.transform(event.getX, event.getY)
              connectableNodePressedWithTransitionItem(connectableNode, point.getX, point.getY)
            case _ =>
              cancelActionWithTransitionItem()
          }
        case _: StateItem =>
          if (event.getSource == this) {
            drawConnectableNode(new State(), event.getX, event.getY)
          }
        case _: StartItem =>
          if (event.getSource == this) {
            drawConnectableNode(new Start(), event.getX, event.getY)
          }
        case _: EndItem =>
          if (event.getSource == this) {
            drawConnectableNode(new End(), event.getX, event.getY)
          }
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
    event.consume()
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

  private def mouseMovedWithTransitionItem(deltaX: Double, deltaY: Double): Unit = {
    if (temporalTransition.isDefined) {
      val transition = temporalTransition.get
      val tempNode = transition.node2

      tempNode.drag(deltaX, deltaY)
      transition.redraw()
    }
  }

  private def connectableNodePressedWithTransitionItem(connectableNode: ConnectableNode, x: Double, y:Double): Unit = {
    if (temporalTransition.isEmpty) {
      val tempNode = GhostNode()
      tempNode.setTranslateX(x)
      tempNode.setTranslateY(y)
      tempNode.setVisible(false)
      drawNode(tempNode)

      val transition = new Transition(connectableNode, tempNode)
      drawNode(transition)
      transition.toBack()

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

  private def cancelActionWithTransitionItem(): Unit = {
    if (temporalTransition.isDefined) {
      val transition = temporalTransition.get
      val tempNode = transition.node2

      eraseNode(transition)
      eraseNode(tempNode)

      temporalTransition = None
    }
  }

  private def eraseConnectableNode(connectableNode: ConnectableNode): Unit = {
    //Retrieve the transitions of the node
    connectableNode.transitions.foreach(transition => {
      val otherNode = {
        if (connectableNode != transition.node1)
          transition.node1
        else
          transition.node2
      }

      otherNode.removeTransition(transition)
      eraseNode(transition)
    })
    eraseNode(connectableNode)
    toolBox.setToolToDefault()
  }

  private def eraseNode(node: Node): Unit = {
    getChildren.remove(node)
  }

  private def updateMousePosition(event: MouseEvent): Unit = {
    mouseX = event.getSceneX
    mouseY = event.getSceneY
  }

  private def drawNode(shape: Node): Unit = {
    shape.setOnMousePressed(mousePressed())
    shape.setOnMouseDragged(mouseDragged())
    getChildren.add(shape)
  }
}
