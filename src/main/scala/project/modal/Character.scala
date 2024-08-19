package project.modal

import scalafx.Includes.handle
import scalafx.scene.image.{Image, ImageView}
import scalafx.animation.PauseTransition
import scalafx.util.Duration

class Character(imageView: ImageView, characterType: String) {

  // Load images for different states of the character
  private val idleGif: Image = new Image(getClass.getResourceAsStream(s"/project/images/$characterType/$characterType-idle.gif"))
  private val attackGif: Image = new Image(getClass.getResourceAsStream(s"/project/images/$characterType/$characterType-attack.gif"))
  private val hurtGif: Image = new Image(getClass.getResourceAsStream(s"/project/images/$characterType/$characterType-hurt.gif"))
  private val deathGif: Image = new Image(getClass.getResourceAsStream(s"/project/images/$characterType/$characterType-death.gif"))
  private val knightKillGif: Image = new Image(getClass.getResourceAsStream(s"/project/images/knight/knight-kill.gif"))

  // Variable to track if the character is currently animating
  private var isAnimating = false

  // Duration of the pause before returning to idle state
  private val animationDuration = Duration(500) // 500 milliseconds

  // PauseTransition used to manage the delay before returning to idle state
  private val sharedPauseTransition = new PauseTransition(animationDuration) {
    onFinished = handle {
      setIdle() // Return to idle state
      isAnimating = false // Reset animation state
    }
  }

  // Initiates the pause transition to return to idle state if not already animating
  private def returnIdle(): Unit = {
    if (!isAnimating) {
      isAnimating = true
      sharedPauseTransition.play() // Start the delay
    }
  }

  // Sets the character to its idle state
  def setIdle(): Unit = {
    imageView.image = idleGif
    characterType match {
      case "knight" =>
        imageView.setFitHeight(140)
        imageView.setFitWidth(140)
      case "bandit" =>
        imageView.setFitHeight(140)
        imageView.setFitWidth(110)
    }
  }

  // Sets the character to its attack state and initiates return to idle state
  def performAttack(): Unit = {
    imageView.image = attackGif
    characterType match {
      case "knight" =>
        imageView.setFitHeight(210)
        imageView.setFitWidth(230)
      case "bandit" =>
        imageView.setFitHeight(150)
        imageView.setFitWidth(140)
    }
    returnIdle()
  }

  // Sets the knight to its kill state and initiates return to idle state
  def performKill(): Unit = {
    imageView.image = knightKillGif
    imageView.setFitHeight(210)
    imageView.setFitWidth(230)
    returnIdle()
  }

  // Sets the character to its hurt state and initiates return to idle state
  def performHurt(): Unit = {
    imageView.image = hurtGif
    characterType match {
      case "knight" =>
        imageView.setFitHeight(150)
        imageView.setFitWidth(140)
      case "bandit" =>
        imageView.setFitHeight(130)
        imageView.setFitWidth(105)
    }
    returnIdle()
  }

  // Sets the bandit to its death state and initiates return to idle state
  def banditDeath(): Unit = {
    imageView.image = deathGif
    imageView.setFitHeight(180)
    imageView.setFitWidth(140)
    returnIdle()
  }

  // Sets the knight to its death state with a completion callback
  def knightDeath(onComplete: () => Unit): Unit = {
    imageView.image = deathGif
    imageView.setFitHeight(210)
    imageView.setFitWidth(220)
    val deathTransition = new PauseTransition(Duration(500)) {
      onFinished = handle {
        setIdle()
        onComplete() // Execute the callback function when death animation completes
      }
    }
    deathTransition.play()
  }
}
