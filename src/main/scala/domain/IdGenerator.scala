package domain

object IdGenerator {
  var nextId = 0

  def getId: String = {
    val id = nextId

    nextId = nextId + 1

    id.toString
  }
}
