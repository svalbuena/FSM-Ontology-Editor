package infrastructure.filechooser

import javafx.stage.{FileChooser, Stage}
import javafx.stage.FileChooser.ExtensionFilter

class FsmFileChooser(stage: Stage) {
  private val fileChooser = new FileChooser
  fileChooser.setTitle("Choose the fsm file")
  fileChooser.getExtensionFilters.addAll(
    new ExtensionFilter("RDF files", "*.ttl")
  )

  def askForFilename(): Option[String] = {
    var filenameOption: Option[String] = None

    val selectedFile = fileChooser.showOpenDialog(stage)
    if (selectedFile != null) {
      val filename = selectedFile.getAbsolutePath

      filenameOption = Some(filename)
    }

    filenameOption
  }
}
