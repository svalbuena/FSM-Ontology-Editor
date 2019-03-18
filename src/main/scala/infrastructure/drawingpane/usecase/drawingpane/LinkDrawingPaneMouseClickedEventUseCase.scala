package infrastructure.drawingpane.usecase.drawingpane


import infrastructure.drawingpane.DrawingPane
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent

class LinkDrawingPaneMouseClickedEventUseCase(drawingPane: DrawingPane) {
  def link(eventHandler: EventHandler[MouseEvent]): Unit = {
    drawingPane.setOnMouseClicked(eventHandler)
  }
}
