package project.view

import project.MainApp
import scalafx.scene.control.{ChoiceBox}
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController() {

  def selectDifficulty(): Unit = {
    MainApp.showDifficulty()
  }
}
