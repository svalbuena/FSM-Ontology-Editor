package infrastructure.toolbox.sections.selector

import javafx.scene.control.Label
import javafx.scene.layout.{FlowPane, VBox}
import infrastructure.toolbox.sections.selector.mouses.NormalMouseSelector

object SelectorsSection extends VBox {
  val sectionTitle = new Label
  sectionTitle.setText("Selectors")

  val flowPane = new FlowPane()
  flowPane.getChildren.add(NormalMouseSelector)

  getChildren.add(sectionTitle)
  getChildren.add(flowPane)
}
