package project.view

import scalafx.scene.image.Image
import scalafx.scene.input.KeyCode

import scala.util.Random

class DirectionHandler {
  private val arrowKeys = List(KeyCode.Left, KeyCode.Right, KeyCode.Up, KeyCode.Down)
  private var expectedArrow: KeyCode = KeyCode.Left

  // Generate a random arrow key
  def generateRandomArrow(): KeyCode = {
    arrowKeys(Random.nextInt(arrowKeys.length))
  }

  // Map each KeyCode to the corresponding arrow image
  def keyCodeToImage(keyCode: KeyCode): Image = keyCode match {
    case KeyCode.Left => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-left.png"))
    case KeyCode.Up => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-up.png"))
    case KeyCode.Right => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-right.png"))
    case KeyCode.Down => new Image(getClass.getResourceAsStream("/project/images/direction/arrow-down.png"))
    case _ => new Image(getClass.getResourceAsStream("/project/images/direction/space.png"))
  }

  // Get the expected arrow
  def getExpectedArrow: KeyCode = expectedArrow
}
