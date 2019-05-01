package domain

/**
  * Generator of numeric ids that starts at 0 and auto-increments by 1 each time an id is generated and returned
  */
object IdGenerator {
  var nextId = 0

  def getId: String = {
    val id = nextId

    nextId = nextId + 1

    id.toString
  }
}
