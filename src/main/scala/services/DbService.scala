package services

trait DbService {

  def init(): Unit

  def getMovie(get: String): String

  def getDescription(get: String): String

  def createMovie(get: String, description: String, link: String): Unit

  def deleteMovie(get: String): Unit
}
