package project.view

import project.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController() {

  def readInstruction() = {
    MainApp.showInstruction()
  }

  def selectDifficulty(): Unit = {
    MainApp.showDifficulty()
  }

  def exit() = {
    System.exit(0)
  }
}
