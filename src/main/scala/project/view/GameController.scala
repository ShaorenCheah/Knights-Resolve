package project.view

import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.text.Text
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.animation.AnimationTimer
import project.modal._
import scalafxml.core.macros.sfxml

import scala.util.Random  // Import Difficulty and its case objects

@sfxml
class GameController(val scoreText: Text, val livesText: Text, val arrowDisplay: Text) {
  private var stage: PrimaryStage = _
  private var difficulty: Difficulty = Easy // Default difficulty
  private var score: Int = 0
  private var multiplier: Double = difficulty.baseMultiplier
  private var combo: Int = 0
  private var lives: Int = difficulty.lives
  private val arrowKeys = List(KeyCode.Left, KeyCode.Right, KeyCode.Up, KeyCode.Down)
  private var expectedArrow: KeyCode = generateRandomArrow()

  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )

  // Generate a random arrow key
  def generateRandomArrow(): KeyCode = {
    arrowKeys(Random.nextInt(arrowKeys.length))
  }

  // Handle user input
  def onKeyPressed(event: KeyEvent): Unit = {
    val keyCode = KeyCode(event.code) // Convert to ScalaFX KeyCode
    if (keyCode == expectedArrow) {
      combo += 1
      multiplier = difficulty.baseMultiplier + (Math.ceil(combo / 10.0) * difficulty.comboIncrement)
      score += (10 * multiplier).toInt
      expectedArrow = generateRandomArrow() // Regenerate the arrow
      arrowDisplay.text = expectedArrow.toString
    } else if (arrowKeys.contains(keyCode) && (event.code != expectedArrow)) {
      combo = 0
      multiplier = difficulty.baseMultiplier
      lives -= 1
    }
    updateUI()
  }

  // Update UI elements
  def updateUI(): Unit = {
    scoreText.text = s"Score: $score"
    livesText.text = s"Lives: $lives"
  }

  // Set the difficulty and update game parameters
  def setDifficulty(difficultyName: String): Unit = {
    println(s"Attempting to set difficulty to: $difficultyName") // Debug statement
    difficultyMap.get(difficultyName) match {
      case Some(difficulty) =>
        this.difficulty = difficulty
        // Update game parameters based on new difficulty
        this.lives = difficulty.lives
        this.multiplier = difficulty.baseMultiplier
        this.combo = 0
        updateUI()
      case None =>
        throw new IllegalArgumentException(s"Invalid difficulty level: $difficultyName")
    }
  }

  // Start the game loop
  def startGame(): Unit = {
    expectedArrow = generateRandomArrow()
    arrowDisplay.text = expectedArrow.toString // Display the initial arrow

    val timer = AnimationTimer { _ =>
      if (lives <= 0) {
        endGame()
      }
    }
    timer.start()
  }

  // End the game and display final score
  def endGame(): Unit = {
    arrowDisplay.text = s"Game Over! Final Score: $score"
  }

  // Method to set the primary stage for the controller
  def setStage(stage: PrimaryStage): Unit = {
    this.stage = stage
  }
}
