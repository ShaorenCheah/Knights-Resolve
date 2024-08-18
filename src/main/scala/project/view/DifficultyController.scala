package project.view

import project.MainApp
import project.modal.{Difficulty, Easy, Normal, Hard}
import scalafx.scene.control.ChoiceBox
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class DifficultyController(val difficultyChoice: ChoiceBox[String], val livesText: Text, val multiplierText: Text, val comboText: Text) {

  var difficulty: Difficulty = Easy
  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )


  def initialize(): Unit = {
    difficultyChoice.getItems.addAll("Easy", "Normal", "Hard")
    difficultyChoice.selectionModel().select("Easy") // Set default value as easy
    updateDisplay()
  }

  def updateDisplay(): Unit = {
    val selectedDifficulty = difficultyChoice.getSelectionModel.getSelectedItem
    difficultyMap.get(selectedDifficulty) match {
      case Some(difficulty) =>
        this.difficulty = difficulty
        // Update game parameters based on new difficulty
        livesText.text = s"LIVES: ${difficulty.lives}"
        multiplierText.text = s"BASE MULTIPLIER: ${difficulty.baseMultiplier}"
        comboText.text = s"COMBO INCREMENT: +${difficulty.comboIncrement}"
      case None =>
        println(s"Difficulty level '$selectedDifficulty' is not valid.")
    }
  }


  def startGame(): Unit = {
    val difficulty = difficultyChoice.selectionModel().getSelectedItem
    MainApp.showGame(difficulty)
  }

}
