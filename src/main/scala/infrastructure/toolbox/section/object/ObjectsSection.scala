package infrastructure.toolbox.section.`object`

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import infrastructure.toolbox.section.`object`.fsm._

object ObjectsSection extends VBox {
  val sectionTitle = new Label()
  sectionTitle.setText("Objects")

  getChildren.add(sectionTitle)
  getChildren.add(EndItem)
  getChildren.add(StartItem)
  getChildren.add(StateItem)
  getChildren.add(TransitionItem)
}
