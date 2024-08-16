package project.view

import project.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController {
  def startGame(): Unit = {
    MainApp.showGame()
  }
}
