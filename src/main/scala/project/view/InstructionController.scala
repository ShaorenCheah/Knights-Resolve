package project.view

import project.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class InstructionController {

  def back(): Unit = {
    MainApp.showHomepage()
  }
}
