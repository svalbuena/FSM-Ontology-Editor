package infrastructure.controller

import application.command.fsm.add.AddFsmCommand
import application.commandhandler.fsm.add.AddFsmHandler
import infrastructure.controller.action.{ActionListener, BodyListener, PrototypeParameterListener, PrototypeUriListener}
import infrastructure.controller.condition.ConditionListener
import infrastructure.controller.end.EndListener
import infrastructure.controller.guard.GuardListener
import infrastructure.controller.start.StartListener
import infrastructure.controller.state.StateListener
import infrastructure.controller.transition.TransitionListener
import infrastructure.drawingpane.{DrawingPane, MousePosition}
import infrastructure.elements.action.body.Body
import infrastructure.elements.action.uri.prototype.PrototypeUri
import infrastructure.elements.action.uri.prototype.parameter.PrototypeParameter
import infrastructure.elements.action.{Action, ActionType}
import infrastructure.elements.condition.Condition
import infrastructure.elements.guard.Guard
import infrastructure.elements.node._
import infrastructure.elements.transition.Transition
import infrastructure.id.IdGenerator
import infrastructure.propertybox.PropertiesBox
import infrastructure.toolbox.ToolBox
import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import javafx.event.EventHandler
import javafx.scene.input.{MouseButton, MouseEvent}
import javafx.scene.layout.Pane

class DrawingPaneController(drawingPane: DrawingPane, val toolBox: ToolBox, val propertiesBox: PropertiesBox) {
  private val mousePosition = new MousePosition()

  private var temporalTransition: Option[Transition] = None
  private var ghostElement: Option[GhostElement] = None

  private val canvas = drawingPane.canvas

  private val idGenerator = new IdGenerator
  private val addFsmHandler = new AddFsmHandler

  private var fsmName = addFsmHandler.handle(new AddFsmCommand)

  val prototypeParameter = new PrototypeParameter("SELECT * FROM users", "[user_id]")

  val action1 = new Action(name = "Action" + idGenerator.getId, actionType = ActionType.ENTRY, prototypeUri = new PrototypeUri(name = idGenerator.getId), body = new Body(name = idGenerator.getId))
  val action2 = new Action(name = "Action" + idGenerator.getId, actionType = ActionType.ENTRY, prototypeUri = new PrototypeUri(name = idGenerator.getId), body = new Body(name = idGenerator.getId))


  val state1 = new State(name = idGenerator.getId, actions = List(action1, action2))
  state1.actions.head.prototypeUri.prototypeParameters = prototypeParameter :: state1.actions.head.prototypeUri.prototypeParameters
  val state2 = new State(idGenerator.getId)

  val transition = new Transition("TransitionTest", state1, state2)

  addState(state1, 0, 0)
  addState(state2, 300, 0)
  state1.addOutTransition(transition)
  state2.addInTransition(transition)
  addTransition(transition)

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
          //TODO: notify the model
          println("Adding a state")
          addState(new State(idGenerator.getId), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: StartItem =>
          //TODO: notify the model
          println("Adding a start")
          addStart(new Start(idGenerator.getId), event.getX, event.getY)
          toolBox.setToolToDefault()
        case _: EndItem =>
          //TODO: notify the model
          println("Adding a end")
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

    for (action <- state.actions) {
      addActionToState(action, state)
    }
  }

  def addStart(start: Start, x: Double, y: Double): Unit = {
    canvas.drawConnectableNode(start.shape, x, y)

    new StartListener(start, this, drawingPane, idGenerator)
  }

  def addEnd(end: End, x: Double, y: Double): Unit = {
    canvas.drawConnectableNode(end.shape, x, y)

    new EndListener(end, this, drawingPane)
  }

  def addTransition(transition: Transition): Unit = {
    canvas.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    transition.propertiesBox.setTransitionName(transition.name
    )
    new TransitionListener(transition, this, idGenerator)
  }

  def addActionToState(action: Action, state: State): Unit = {
    addAction(action)

    action.setParent(state)

    state.propertiesBox.addAction(action.propertiesBox, action.actionType)
    state.shape.addAction(action.shape, action.actionType)
  }

  def addActionToGuard(action: Action, guard: Guard): Unit = {
    addAction(action)

    action.setParent(guard)

    guard.propertiesBox.addAction(action.propertiesBox)
    guard.shape.addAction(action.shape)

    if (guard.hasParent) {
      canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
    }
  }

  def addAction(action: Action): Unit = {
    action.propertiesBox.setTiltedPaneName(action.name)
    action.propertiesBox.setActionType(action.actionType)
    action.propertiesBox.setActionName(action.name)
    action.propertiesBox.setMethodType(action.method)
    action.propertiesBox.setUriType(action.uriType)
    action.propertiesBox.setTimeout(action.timeout)
    action.propertiesBox.setAbsoluteUri(action.absoluteUri)

    action.shape.setActionType(action.actionType)
    action.shape.setActionName(action.name)

    addBody(action.body)
    addPrototypeUri(action.prototypeUri)

    new ActionListener(action, this, drawingPane, idGenerator)
  }

  def addGuardToTransition(guard: Guard, transition: Transition): Unit = {
    addGuard(guard)

    guard.setParent(transition)

    transition.propertiesBox.addTransitionGuard(guard.propertiesBox)
    transition.shape.addTransitionGuard(guard.shape)

    canvas.updateTransitionGuardGroupPosition(transition.shape)
  }

  def addGuard(guard: Guard): Unit = {
    guard.propertiesBox.setGuardTitledPaneName(guard.name)
    guard.propertiesBox.setGuardName(guard.name)

    guard.shape.setGuardName(guard.name)

    for (action <- guard.actions) {
      addActionToGuard(action, guard)
    }

    for (condition <- guard.conditions) {
      addConditionToGuard(condition, guard)
    }

    new GuardListener(guard, this, idGenerator)
  }

  def addConditionToGuard(condition: Condition, guard: Guard): Unit = {
    addCondition(condition)

    condition.setParent(guard)

    guard.propertiesBox.addCondition(condition.propertiesBox)
    guard.shape.addCondition(condition.shape)

    if (guard.hasParent) {
      canvas.updateTransitionGuardGroupPosition(guard.getParent.shape)
    }
  }

  def addCondition(condition: Condition): Unit = {
    condition.propertiesBox.setConditionName(condition.name)
    condition.propertiesBox.setConditionQuery(condition.query)

    condition.shape.setConditionName(condition.name)

    new ConditionListener(condition, this)
  }

  def addBody(body: Body): Unit = {
    body.propertiesBox.setBodyType(body.bodyType)
    body.propertiesBox.setBodyContent(body.content)

    new BodyListener(body)
  }

  def addPrototypeUri(prototypeUri: PrototypeUri): Unit = {
    for (prototypeParameter <- prototypeUri.prototypeParameters) {
      addPrototypeUriParameterToPrototypeUri(prototypeParameter, prototypeUri)
    }
    prototypeUri.propertiesBox.setStructure(prototypeUri.structure)

    new PrototypeUriListener(prototypeUri, this)
  }

  def addPrototypeUriParameterToPrototypeUri(prototypeUriParameter: PrototypeParameter, prototypeUri: PrototypeUri): Unit = {
    addPrototypeUriParameter(prototypeUriParameter)

    prototypeUriParameter.setParent(prototypeUri)

    prototypeUri.propertiesBox.addParameter(prototypeUriParameter.propertiesBox)
  }

  def addPrototypeUriParameter(prototypeParameter: PrototypeParameter): Unit = {
    prototypeParameter.propertiesBox.setQuery(prototypeParameter.query)
    prototypeParameter.propertiesBox.setPlaceholder(prototypeParameter.placeholder)

    new PrototypeParameterListener(prototypeParameter, this)
  }

  def addTemporalTransition(source: ConnectableElement, x: Double, y: Double): Unit = {
    ghostElement = Some(new GhostElement(idGenerator.getId))

    canvas.drawConnectableNode(ghostElement.get.shape, x, y)

    val transition = new Transition("Temp Transition", source, ghostElement.get)

    ghostElement.get.addInTransition(transition)

    canvas.drawTransition(transition.shape, transition.getSourceShape, transition.getDestinationShape)

    temporalTransition = Some(transition)
  }

  def establishTemporalTransition(destination: ConnectableElement): Unit = {
    val source = temporalTransition.get.source

    val id = "Transition" + idGenerator.getId

    val newTransition = new Transition(id, source, destination)
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

  def removeConnectableElement(connectableElement: ConnectableElement, connectableShape: Pane): Unit = {
    connectableElement.getTransitions.foreach(transition => removeTransition(transition))
    canvas.eraseConnectableNode(connectableShape)
  }

  def removeTransition(transition: Transition): Unit = {
    transition.source.outTransitions = transition.source.outTransitions.filterNot(t => t == transition)
    transition.destination.inTransitions = transition.destination.inTransitions.filterNot(t => t == transition)

    canvas.eraseTransition(transition.shape)
  }

  def removeActionFromState(action: Action, state: State): Unit = {
    state.propertiesBox.removeAction(action.propertiesBox, action.actionType)
    state.shape.removeAction(action.shape, action.actionType)
  }

  def removeActionFromGuard(action: Action, guard: Guard): Unit = {
    guard.propertiesBox.removeAction(action.propertiesBox)
    guard.shape.removeAction(action.shape)
  }

  def removeGuardFromTransition(guard: Guard, transition: Transition): Unit = {
    transition.propertiesBox.removeTransitionGuard(guard.propertiesBox)
    transition.shape.removeTransitionGuard(guard.shape)
  }

  def removeConditionFromGuard(condition: Condition, guard: Guard): Unit = {
    guard.propertiesBox.removeCondition(condition.propertiesBox)
    guard.shape.removeCondition(condition.shape)
  }

  def removePrototypeUriParameterFromPrototypeUri(prototypeUriParameter: PrototypeParameter, prototypeUri: PrototypeUri): Unit = {
    prototypeUri.propertiesBox.removePrototypeUriParameter(prototypeUriParameter.propertiesBox)
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
