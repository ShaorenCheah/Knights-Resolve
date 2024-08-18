package project.view

import project.MainApp
import project.modal._
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.{KeyCode, KeyEvent}
import scalafx.animation.AnimationTimer
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.FlowPane
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

import scala.collection.mutable.ArrayBuffer

@sfxml
class GameController(val timerText: Text, val multiplierText: Text, val comboText: Text, val scoreText: Text, val livesText: Text, val arrowPane: FlowPane) {

  private var stage: PrimaryStage = _
  private var difficulty: Difficulty = Easy // Default difficulty
  private var score: Int = 0
  private var multiplier: Double = difficulty.baseMultiplier
  private var combo: Int = 0
  private var lives: Int = difficulty.lives
  private def arrowKeys: Set[KeyCode] = Set(KeyCode.Left, KeyCode.Up, KeyCode.Right, KeyCode.Down)

  private val difficultyMap: Map[String, Difficulty] = Map(
    "Easy" -> Easy,
    "Normal" -> Normal,
    "Hard" -> Hard
  )

  // Timer to set game duration
  private val gameDuration: Int = 60
  private var elapsedTime: Long = 0L
  private var timeLeft: Int = gameDuration
  private var timer: AnimationTimer = _

  // Create 5 fixed slots for arrows using ImageView
  private val arrowImages: Seq[ImageView] = Seq.fill(5)(new ImageView())
  private val arrowKeysList: ArrayBuffer[KeyCode] = ArrayBuffer.fill(5)(KeyCode.Space) // Initialize with default KeyCode

  // Add all ImageView components to the FlowPane
  arrowImages.foreach(imageView => {
    imageView.setFitHeight(50) // Default image height
    imageView.setFitWidth(50)  // Default image width
    arrowPane.children.add(imageView)
  })
  arrowPane.alignment = Pos.Center
  arrowPane.hgap = 10

  // Method to set the primary stage for the controller
  def setStage(stage: PrimaryStage): Unit = {
    this.stage = stage
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

  // Initialize DirectionHandler
  private val directionHandler = new Direction()

  // Initialize the arrow sequence
  private def generateInitialArrows(): Unit = {
    val initialArrows = List.fill(3)(directionHandler.generateRandomArrow()) ++ List.fill(2)(KeyCode.Space)
    for (i <- initialArrows.indices) {
      arrowKeysList(i) = initialArrows(i) // Update the KeyCode list
      arrowImages(i).image = directionHandler.keyCodeToImage(initialArrows(i)) // Set the image
      // Reset image size for all arrows initially
      arrowImages(i).setFitHeight(50)
      arrowImages(i).setFitWidth(50)
    }
    // Set the expected arrow as the middle arrow
    updateExpectedArrow() // Middle one is the expected one
  }

  // Update the arrow sequence
  def updateArrows(): Unit = {
    // Generate a new arrow
    val newArrow = directionHandler.generateRandomArrow()

    // Shift arrows to the right, update the KeyCode list
    for (i <- (arrowImages.length - 1) to 1 by -1) {
      arrowKeysList(i) = arrowKeysList(i - 1) // Update KeyCode list
    }
    arrowKeysList(0) = newArrow // Add new arrow to the front of KeyCode list

    // Update images based on the updated KeyCode list
    arrowImages.zip(arrowKeysList).foreach { case (imageView, keyCode) =>
      imageView.image = directionHandler.keyCodeToImage(keyCode) // Set image based on KeyCode
      imageView.setFitHeight(50) // Set default size for all arrows
      imageView.setFitWidth(50)
      imageView.margin = Insets(10)
    }

    // Update the expected arrow
    updateExpectedArrow()
  }


  // Retrieve the expected arrow from the list
  private def expectedArrow: KeyCode = arrowKeysList(2)

  // Set the expected arrow and enlarge it
  private def updateExpectedArrow(): Unit = {
    for (i <- arrowImages.indices) {
      // Only enlarge the middle arrow
      if (i == 2 && expectedArrow == arrowKeysList(2)) {
        arrowImages(i).setFitHeight(100) // Enlarge the expected arrow
        arrowImages(i).setFitWidth(100)
      } else {
        arrowImages(i).setFitHeight(50) // Reset image size for non-expected arrows
        arrowImages(i).setFitWidth(50)
      }
    }
  }

  // Handle user input
  def onKeyPressed(event: KeyEvent): Unit = {
    if (event.code == expectedArrow) {
      combo += 1
      multiplier = difficulty.baseMultiplier + (Math.ceil(combo / 10.0) * difficulty.comboIncrement)
      score += (10 * multiplier).toInt
      updateArrows()
    } else if (arrowKeys.contains(event.code) && event.code != expectedArrow) {
      combo = 0
      multiplier = difficulty.baseMultiplier
      lives -= 1
    }
    updateUI()
  }

  // Update UI elements
  private def updateUI(): Unit = {
    timerText.text = s"$timeLeft s"
    multiplierText.text = f"Multiplier: x$multiplier%.1f"
    comboText.text = s"COMBO: $combo"
    scoreText.text = s"Score: $score"
    livesText.text = s"Lives: $lives"
  }

  // Start the game loop
  def startGame(): Unit = {
    generateInitialArrows() // Display the initial sequence of arrows

    timer = AnimationTimer { now =>
      if (elapsedTime == 0L) elapsedTime = now // Initialize elapsed time
      val currentTime = (now - elapsedTime) / 1e9.toInt
      timeLeft = (gameDuration.toLong - currentTime).toInt
      if (lives <= 0 || timeLeft <= 0) {
        timer.stop() // Stop the AnimationTimer
        endGame()
      }
      updateUI()
    }

    timer.start()
  }

  // End the game and display final score
  private def endGame(): Unit = {
    MainApp.showResult(this.score)
  }
}
