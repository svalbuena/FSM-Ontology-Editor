package infrastructure.drawingpane.shape.guard

import javafx.scene.layout.VBox

/**
  * Visual group of the guards of a transition
  */
class GuardGroupShape extends VBox {
  /**
    * Adds a guard to the group
    *
    * @param guardPane the guard pane to add
    */
  def addGuard(guardPane: GuardPane): Unit = getChildren.add(guardPane)

  /**
    * Removes a guard from the group
    *
    * @param guardPane the guard pane to remove
    */
  def removeGuard(guardPane: GuardPane): Unit = getChildren.remove(guardPane)
}
