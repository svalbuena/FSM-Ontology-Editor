package toolbox

import javafx.scene.control.Label
import javafx.scene.layout.VBox
import propertybox.PropertiesBox.setStyle
import toolbox.sections.`object`.ObjectsSection
import toolbox.sections.selector.SelectorsSection

object ToolBox extends VBox {
  setStyle("-fx-background-color: #b3c6b3")

  val toolBoxTitle = new Label()
  toolBoxTitle.setText("ToolBox")

  getChildren.add(toolBoxTitle)
  getChildren.add(SelectorsSection)
  getChildren.add(ObjectsSection)
}
