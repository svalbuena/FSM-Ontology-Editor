package infrastructure.toolbox

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import infrastructure.propertybox.PropertiesBox.setStyle
import infrastructure.toolbox.sections.`object`.ObjectsSection
import infrastructure.toolbox.sections.selector.SelectorsSection

object ToolBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val toolBoxTitle = new Label()
  toolBoxTitle.setText("ToolBox")

  getChildren.add(toolBoxTitle)
  getChildren.add(SelectorsSection)
  getChildren.add(ObjectsSection)
}
