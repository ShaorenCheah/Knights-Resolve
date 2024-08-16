package project.view

import project.MainApp
import scalafx.scene.control.{Button, ChoiceBox}
import scalafxml.core.macros.sfxml

@sfxml
class HomepageController(val difficultyChoice: ChoiceBox[String]) {

  def initialize(): Unit = {
    difficultyChoice.getItems.addAll("Easy", "Normal", "Hard")
    difficultyChoice.selectionModel().select("Easy") // Set default value if needed
  }

  def startGame(): Unit = {
    val difficulty = difficultyChoice.selectionModel().getSelectedItem
    MainApp.showGame(difficulty)
  }
}
