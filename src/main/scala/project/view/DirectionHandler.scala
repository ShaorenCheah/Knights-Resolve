package project.view

import scalafx.scene.input.KeyCode
import scala.util.Random

class DirectionHandler {
  private val arrowKeys = List(KeyCode.Left, KeyCode.Right, KeyCode.Up, KeyCode.Down)
  private var expectedArrow: KeyCode = KeyCode.Left

  // Generate a random arrow key
  def generateRandomArrow(): KeyCode = {
    arrowKeys(Random.nextInt(arrowKeys.length))
  }

  // Convert KeyCode to symbol
  def keyCodeToSymbol(keyCode: KeyCode): String = keyCode match {
    case KeyCode.Left => "←"
    case KeyCode.Up => "↑"
    case KeyCode.Right => "→"
    case KeyCode.Down => "↓"
    case _ => ""
  }

  // Get the expected arrow
  def getExpectedArrow: KeyCode = expectedArrow
}
