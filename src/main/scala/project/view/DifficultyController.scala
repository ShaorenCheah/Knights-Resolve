package project.view

import project.MainApp
import project.modal.{Audio, Difficulty, Easy, Hard, Normal}
import scalafx.Includes.jfxObservableValue2sfx
import scalafx.scene.control.ChoiceBox
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

@sfxml
class DifficultyController(val difficultyChoice: ChoiceBox[String], val livesText: Text, val multiplierText: Text, val comboText: Text) {
  Audio.playBgm("/project/audio/difficulty.mp3")

  // Set default difficulty as Easy from case object
  var difficulty: Difficulty = Easy

  // Map difficulty from its corresponding String input to case object
  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )

  // Initialize the UI for Difficulty.fxml
  def initialize(): Unit = {
    difficultyChoice.getItems.addAll("Easy", "Normal", "Hard")
    // Set up a watcher for changes in the selected item
    difficultyChoice.selectionModel().selectedItemProperty().onChange {
      (_, _, newValue) =>
        updateDisplay() // Update display whenever the selected difficulty changes
    }
    difficultyChoice.selectionModel().select("Easy") // Set default value as easy
  }

  // Update difficulty information displayed when a new one is selected
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
    Audio.buttonClick()
  }

  // Call start game method in main and pass selected difficulty
  def startGame(): Unit = {
    val difficulty = difficultyChoice.selectionModel().getSelectedItem
    Audio.buttonClick()
    MainApp.showGame(difficulty)
  }

}
