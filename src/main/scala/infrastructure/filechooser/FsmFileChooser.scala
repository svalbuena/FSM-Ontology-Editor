package infrastructure.filechooser

import javafx.stage.{FileChooser, Stage}
import javafx.stage.FileChooser.ExtensionFilter

class FsmFileChooser(stage: Stage) {
  private val fileChooser = new FileChooser

  fileChooser.getExtensionFilters.addAll(
    new ExtensionFilter("RDF files", "*.ttl")
  )

  def askForFileToOpen(): Option[String] = {
    var filenameOption: Option[String] = None

    fileChooser.setTitle("Choose the FSM file to load")

    val selectedFile = fileChooser.showOpenDialog(stage)
    if (selectedFile != null) {
      val filename = selectedFile.getAbsolutePath

      filenameOption = Some(filename)
    }

    filenameOption
  }

  def askForFileToSave(): Option[String] = {
    var filenameOption: Option[String] = None

    fileChooser.setTitle("Choose where to save the FSM")

    val selectedFile = fileChooser.showSaveDialog(stage)
    if (selectedFile != null) {
      val filename = selectedFile.getAbsolutePath

      filenameOption = Some(filename)
    }

    filenameOption
  }
}
