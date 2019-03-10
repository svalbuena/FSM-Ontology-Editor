package infrastructure.toolbox.section.item

import infrastructure.toolbox.section.item.fsm.{EndItem, StartItem, StateItem, TransitionItem}
import javafx.scene.control.{Label, ToggleGroup}
import javafx.scene.layout.VBox

class ItemsSection(toggleGroup: ToggleGroup) extends VBox {
  val sectionTitle = new Label()
  sectionTitle.setText("Objects")

  val itemList = List(new EndItem, new StartItem, new StateItem, new TransitionItem)

  getChildren.add(sectionTitle)

  itemList.foreach(item => {
    item.setToggleGroup(toggleGroup)
    getChildren.add(item)
  })
}
