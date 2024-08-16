package project.view

import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

import scala.util.Random

@sfxml
class GameController(val scoreText: Text, val livesText: Text, val arrowDisplay: Text) {
  private var stage: PrimaryStage = _

  // Game state variables
  var score: Int = 0
  var multiplier: Int = 1
  var combo: Int = 0
  var lives: Int = 3

  private val arrowKeys = List(KeyCode.Left, KeyCode.Right, KeyCode.Up, KeyCode.Down)
  private var expectedArrow: KeyCode = generateRandomArrow()

  // Generate a random arrow key
  def generateRandomArrow(): KeyCode = {
    arrowKeys(Random.nextInt(arrowKeys.length))
  }

  // Handle user input
  def onKeyPressed(event: KeyEvent): Unit = {
    println("Key pressed: " + event.code)
    val keyCode = KeyCode(event.code) // Convert to ScalaFX KeyCode
    if (event.code == expectedArrow) {
      // Correct arrow pressed
      combo += 1
      multiplier = combo / 10 + 1
      score += 10 * multiplier

      // Generate new expected arrow and update UI
      expectedArrow = generateRandomArrow()
      arrowDisplay.text = expectedArrow.toString

    } else if (arrowKeys.contains(keyCode) && (event.code != expectedArrow)) {
      // Incorrect arrow pressed
      combo = 0
      multiplier = 1
      lives -= 1
    }
    updateUI()
  }

  // Update UI elements
  def updateUI(): Unit = {
    scoreText.text = s"Score: $score"
    livesText.text = s"Lives: $lives"
  }

  // Start the game loop
  def startGame(): Unit = {
    arrowDisplay.text = expectedArrow.toString // Display the initial arrow

    val timer = AnimationTimer { _ =>
      if (lives <= 0) {
        endGame()
      }
    }
    timer.start()
  }

  // Method to set the primary stage for the controller
  def setStage(stage: PrimaryStage): Unit = {
    this.stage = stage
  }

  // End the game and display final score
  def endGame(): Unit = {
    arrowDisplay.text = s"Game Over! Final Score: $score"
  }
}
