package project.view

import project.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController {
  def returnHome(): Unit = {
    MainApp.showHomepage()
  }

  def viewInstruction() = {
    MainApp.showInstruction()
  }

  def exit() = {
    System.exit(0)
  }
}
