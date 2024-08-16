package project.modal

import project.view.GameController

// Difficulty.scala
trait Difficulty {
  def lives: Int
  def baseMultiplier: Double
  def comboIncrement: Double
}

// Define case classes for each difficulty level
case object Easy extends Difficulty {
  val lives: Int = 5
  val baseMultiplier: Double = 1.0
  val comboIncrement: Double = 0.05
}

case object Normal extends Difficulty {
  val lives: Int = 3
  val baseMultiplier: Double = 1.2
  val comboIncrement: Double = 0.1
}

case object Hard extends Difficulty {
  val lives: Int = 1
  val baseMultiplier: Double = 1.4
  val comboIncrement: Double = 0.15
}
