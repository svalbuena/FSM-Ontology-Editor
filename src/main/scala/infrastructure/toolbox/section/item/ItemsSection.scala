package infrastructure.toolbox.section.item

import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.VBox

class ItemsSection(toggleGroup: ToggleGroup) extends VBox {
  val endItem = new EndItem
  val startItem = new StartItem
  val stateItem = new StateItem
  val transitionItem = new TransitionItem

  val itemList = List(endItem, startItem, stateItem, transitionItem)

  for (item <- itemList) {
    item.setOnAction(_ => {
      if (!item.isSelected) item.setSelected(true)
    })
    item.setToggleGroup(toggleGroup)
    getChildren.add(item)
  }

  setStyle()


  private def setStyle(): Unit = {
    for (item <- itemList) {
      item.prefWidthProperty().bind(widthProperty())
    }
  }
}
