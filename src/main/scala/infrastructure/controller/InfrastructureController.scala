package infrastructure.controller

import infrastructure.controller.action.{ActionListener, BodyListener, PrototypeParameterListener, PrototypeUriListener}
import infrastructure.controller.node.{EndListener, StartListener, StateListener}
import infrastructure.drawingpane.{DrawingPane, MousePosition}
import infrastructure.drawingpane.shape._
import infrastructure.elements.action.body.Body
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.node._
import infrastructure.elements.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.menu.contextmenu.state.item.{AddEntryActionMenuItem, AddExitActionMenuItem}
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import infrastructure.toolbox.section.selector.mouse.{DeleteMouseSelector, NormalMouseSelector}
import javafx.event.EventHandler
import javafx.scene.input.{MouseButton, MouseEvent}

class InfrastructureController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBox) {
  private val mousePosition = new MousePosition()

  private var temporalTransition: Option[Transition] = None
  private var ghostElement: Option[GhostElement] = None

  private val canvas = drawingPane.canvas

  private val idGenerator = new IdGenerator

  val state1 = new State(id = idGenerator.getId, entryActions = List(new Action(idGenerator.getId, ActionType.ENTRY,"Action 1"), new Action(idGenerator.getId, ActionType.ENTRY, "Action 2")))
  val state2 = new State(idGenerator.getId)

  addState(state1, 0, 0)
  addState(state2, 300, 0)

  drawingPane.setOnMouseClicked(drawingPaneMouseClickedListener)
  drawingPane.setOnMouseMoved(drawingPaneMouseMovedListener)


  def drawingPaneMouseClickedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    updateMousePosition(event)

    if (event.getButton == MouseButton.PRIMARY) {
      toolBox.getSelectedTool match {
        case _: TransitionItem =>
          if (temporalTransition.isDefined) {
            cancelTransitionCreation()
            toolBox.setToolToDefault()
          }
        case _: StateItem =>
          addState(new State(idGenerator.getId), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: StartItem =>
          addStart(new Start(idGenerator.getId), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: EndItem =>
          addEnd(new End(idGenerator.getId), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _ =>
      }
    }
  }

  def drawingPaneMouseMovedListener: EventHandler[MouseEvent] = (event: MouseEvent) => {
    toolBox.getSelectedTool match {
      case _: TransitionItem =>
        if (temporalTransition.isDefined) {
          val (deltaX, deltaY) = calculateDeltaFromMouseEvent(event)
          dragTemporalTransition(deltaX, deltaY)
        }
      case _ =>
    }

    updateMousePosition(event)
  }

  def addState(state: State, x: Double, y: Double): Unit = {
    new StateListener(state, this, drawingPane, idGenerator)

    state.propertiesBox.setName(state.name)

    state.shape.setName(state.name)
    canvas.drawConnectableNode(state.shape, x, y)

    for (action <- state.entryActions ::: state.exitActions) {
      addActionState(action, state)
    }
  }

  def addStart(start: Start, x: Double, y: Double): Unit = {
    new StartListener(start, this, drawingPane, idGenerator)
    canvas.drawConnectableNode(start.shape, x, y)
  }

  def addEnd(end: End, x: Double, y: Double): Unit = {
    new EndListener(end, this, drawingPane)
    canvas.drawConnectableNode(end.shape, x, y)
  }

  def addTransition(transition: Transition): Unit = {
    canvas.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)
  }

  def addActionState(action: Action, state: State): Unit = {
    new ActionListener(action, this, drawingPane, idGenerator)

    action.actionType match {
      case infrastructure.elements.action.ActionType.ENTRY =>
        state.propertiesBox.addEntryAction(action.propertiesBox)
        state.shape.addEntryAction(action.stateActionPane)

      case infrastructure.elements.action.ActionType.EXIT =>
        state.propertiesBox.addExitAction(action.propertiesBox)
        state.shape.addExitAction(action.stateActionPane)
    }
    action.propertiesBox.setActionType(action.actionType)
    action.propertiesBox.setActionName(action.name)
    action.propertiesBox.setUriType(action.uriType)
    action.propertiesBox.setAbsoluteUri(action.absoluteUri)

    action.stateActionPane.setActionType(action.actionType)
    action.stateActionPane.setActionName(action.name)

    addBody(action.body)
    addPrototypeUri(action.prototypeUri)
  }

  def addBody(body: Body): Unit = {
    new BodyListener(body)

    body.propertiesBox.setBodyType(body.bodyType)
    body.propertiesBox.setBodyContent(body.content)
  }

  def addPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    new PrototypeUriListener(prototypeUri)

    for (prototypeParameter <- prototypeUri.prototypeParameters) {
      prototypeUri.propertiesBox.addParameter(prototypeParameter)
      addPrototypeParameter(prototypeParameter)
    }
    prototypeUri.propertiesBox.setStructure(prototypeUri.structure)
  }

  def addPrototypeParameter(prototypeParameter: PrototypeParameter): Unit = {
    new PrototypeParameterListener(prototypeParameter)

    prototypeParameter.propertiesBox.setQuery(prototypeParameter.query)
    prototypeParameter.propertiesBox.setPlaceholder(prototypeParameter.placeholder)
  }

  def addTemporalTransition(source: ConnectableElement, x: Double, y: Double): Unit = {
    ghostElement = Some(new GhostElement(idGenerator.getId))

    canvas.drawConnectableNode(ghostElement.get.shape, x, y)

    val transition = new Transition(idGenerator.getId, source, ghostElement.get)

    ghostElement.get.addInTransition(transition)

    canvas.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    temporalTransition = Some(transition)
  }

  def establishTemporalTransition(destination: ConnectableElement): Unit = {
    val source = temporalTransition.get.source

    val newTransition = new Transition(idGenerator.getId, source, destination)
    source.addOutTransition(newTransition)
    destination.addInTransition(newTransition)

    canvas.eraseTransition(temporalTransition.get.shape)
    addTransition(newTransition)

    temporalTransition = None
  }

  def dragTemporalTransition(deltaX: Double, deltaY: Double): Unit = {
    canvas.dragConnectableNode(ghostElement.get.shape, deltaX, deltaY)
    canvas.dragTransition(temporalTransition.get.shape, temporalTransition.get.getSourceShape, temporalTransition.get.getDestinationShape)
  }

  def cancelTransitionCreation(): Unit = {
    canvas.eraseTransition(temporalTransition.get.shape)
    canvas.eraseConnectableNode(ghostElement.get.shape)

    temporalTransition = None
    ghostElement = None
  }

  def removeConnectableElement(connectableElement: ConnectableElement, connectableShape: ConnectableShape): Unit = {
    connectableElement.getTransitions.foreach(transition => removeTransition(transition))
    canvas.eraseConnectableNode(connectableShape)
  }

  def removeTransition(transition: Transition): Unit = {
    transition.source.outTransitions = transition.source.outTransitions.filterNot(t => t == transition)
    transition.destination.inTransitions = transition.destination.inTransitions.filterNot(t => t == transition)

    canvas.eraseTransition(transition.shape)
  }

  def updateMousePosition(event: MouseEvent): Unit = {
    mousePosition.x = event.getSceneX
    mousePosition.y = event.getSceneY
  }

  def calculateDeltaFromMouseEvent(event: MouseEvent): (Double, Double) = {
    (event.getSceneX - mousePosition.x, event.getSceneY - mousePosition.y)
  }

  def isTemporalTransitionDefined: Boolean = temporalTransition.isDefined
}
