package infrastructure.drawingpane.usecase

import infrastructure.drawingpane.DrawingPane
import infrastructure.drawingpane.usecase.connectablenode.{DragConnectableNodeUseCase, DrawConnectableNodeUseCase, EraseConnectableNodeUseCase}
import infrastructure.drawingpane.usecase.drawingpane.{LinkDrawingPaneMouseClickedEventUseCase, LinkDrawingPaneMouseMovedUseCase}
import infrastructure.drawingpane.usecase.transition.{DrawTransitionUseCase, EraseTransitionUseCase}

class UseCaseContainer(drawingPane: DrawingPane) {
  val drawConnectableNodeUseCase = new DrawConnectableNodeUseCase(drawingPane)
  val dragConnectableNodeUseCase = new DragConnectableNodeUseCase(drawingPane)
  val eraseConnectableNodeUseCase = new EraseConnectableNodeUseCase(drawingPane)
  val drawTransitionUseCase = new DrawTransitionUseCase(drawingPane)
  val eraseTransitionUseCase = new EraseTransitionUseCase(drawingPane)
  val linkDrawingPaneMouseClickedEventUseCase = new LinkDrawingPaneMouseClickedEventUseCase(drawingPane)
  val linkDrawingPaneMouseMovedEventUseCase = new LinkDrawingPaneMouseMovedUseCase(drawingPane)
}
