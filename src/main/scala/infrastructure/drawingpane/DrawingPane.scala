package infrastructure.drawingpane

import infrastructure.drawingpane.shape._
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.{Event, EventHandler}
import javafx.scene.Node
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane

class DrawingPane(toolBox: ToolBox) extends Pane {
  private var mouseX: Double = 0.0
  private var mouseY: Double = 0.0

  private var temporalTransition: Option[Transition] = None

  val state1 = new State()
  val state2 = new State()


  drawState(state1, 10, 10)
  drawState(state2, 300, 0)


  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")
  state1.addAction("entry/Action 1")

  state1.addAction("entry/Action 1")

  setOnMousePressed((event: MouseEvent) => {
    if (event.getButton == MouseButton.PRIMARY) {
      updateMousePosition(event)

      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          println("Drawing pane pressed with trans tool")
          drawingPanePressedWithTransitionTool()
        case _: StateItem =>
          drawState(new State(), event.getX, event.getY)
        case _: StartItem =>
          drawStart(new Start(), event.getX, event.getY)
        case _: EndItem =>
          drawEnd(new End(), event.getX, event.getY)
        case _ =>
      }
    }
  })

  setOnMouseClicked((event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
        mouseMovedWithTransitionItem(deltaX, deltaY)
      case _ =>
    }

    updateMousePosition(event)
  })

  private def mouseMovedWithTransitionItem(deltaX: Double, deltaY: Double): Unit = {
    if (temporalTransition.isDefined) {
      val transition = temporalTransition.get
      val tempNode = transition.node2

      connectableNodeDragged(tempNode, deltaX, deltaY)
      transition.redraw()
    }
  }

  private def connectableNodePressedWithTransitionTool(connectableNode: ConnectableNode, x: Double, y:Double): Unit = {
    if (temporalTransition.isEmpty) {
      println("Creating new transition")
      val ghostNode = GhostNode()

      drawGhostNode(ghostNode, x, y)

      val transition = new Transition(connectableNode, ghostNode)
      getChildren.add(transition)
      transition.toBack()

      temporalTransition = Some(transition)
    } else {
      println("Establishing")
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

  private def drawingPanePressedWithTransitionTool(): Unit = {
    if (temporalTransition.isDefined) {
      val transition = temporalTransition.get
      val tempNode = transition.node2

      eraseNode(transition)
      eraseNode(tempNode)

      temporalTransition = None
    }
  }

  private def connectableNodePressedWithDeleteTool(connectableNode: ConnectableNode): Unit = {
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

  private def drawState(state: State, x: Double, y: Double): Unit = {
    state.setOnMouseDragged((event: MouseEvent) => {
      event.consume()

      val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
      connectableNodeDragged(state, deltaX, deltaY)

      updateMousePosition(event)
    })

    state.setOnMouseClicked((event: MouseEvent) => {
      event.consume()
      updateMousePosition(event)

      if (event.getButton == MouseButton.PRIMARY) {
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            val point = state.getLocalToParentTransform.transform(event.getX, event.getY)
            connectableNodePressedWithTransitionTool(state, point.getX, point.getY)
          case _: DeleteMouseSelector =>
            connectableNodePressedWithDeleteTool(state)
          case _ =>
        }
      }
    })

    state.setTranslateX(x)
    state.setTranslateY(y)

    getChildren.add(state)

    toolBox.setToolToDefault()
  }

  private def drawStart(start: Start, x: Double, y: Double): Unit = {
    start.setOnMouseDragged((event: MouseEvent) => {
      event.consume()

      val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
      connectableNodeDragged(start, deltaX, deltaY)

      updateMousePosition(event)
    })

    start.setOnMouseClicked((event: MouseEvent) => {
      event.consume()
      updateMousePosition(event)

      if (event.getButton == MouseButton.PRIMARY) {
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            val point = start.getLocalToParentTransform.transform(event.getX, event.getY)
            connectableNodePressedWithTransitionTool(start, point.getX, point.getY)
          case _: DeleteMouseSelector =>
            connectableNodePressedWithDeleteTool(start)
          case _ =>
        }
      }
    })

    start.setTranslateX(x)
    start.setTranslateY(y)

    getChildren.add(start)

    toolBox.setToolToDefault()
  }

  private def drawEnd(end: End, x: Double, y: Double): Unit = {
    end.setOnMouseDragged((event: MouseEvent) => {
      event.consume()

      val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
      connectableNodeDragged(end, deltaX, deltaY)

      updateMousePosition(event)
    })

    end.setOnMouseClicked((event: MouseEvent) => {
      event.consume()
      updateMousePosition(event)

      if (event.getButton == MouseButton.PRIMARY) {
        toolBox.getSelectedTool match {
          case _: TransitionItem =>
            val point = end.getLocalToParentTransform.transform(event.getX, event.getY)
            connectableNodePressedWithTransitionTool(end, point.getX, point.getY)
          case _: DeleteMouseSelector =>
            connectableNodePressedWithDeleteTool(end)
          case _ =>
        }
      }
    })

    end.setTranslateX(x)
    end.setTranslateY(y)

    getChildren.add(end)

    toolBox.setToolToDefault()
  }

  private def drawGhostNode(ghostNode: GhostNode, x: Double, y:Double): Unit = {
    ghostNode.setTranslateX(x)
    ghostNode.setTranslateX(y)

    ghostNode.setVisible(false)

    getChildren.add(ghostNode)
  }

  private def connectableNodeDragged(connectableNode: ConnectableNode, deltaX: Double, deltaY: Double): Unit = {
    Option(connectableNode.getBoundsInParent).foreach { shapeBounds =>
      val newX = connectableNode.getTranslateX + deltaX
      val newY = connectableNode.getTranslateY + deltaY
      val parentBounds = connectableNode.getParent.getLayoutBounds

      if (parentBounds.contains(newX, newY, shapeBounds.getWidth, shapeBounds.getHeight)) {
        connectableNode.setTranslateX(newX)
        connectableNode.setTranslateY(newY)
        connectableNode.transitions.foreach(transition => transition.redraw())
      }
    }
  }

  private def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mouseX, event.getSceneY - mouseY)
  }
}
