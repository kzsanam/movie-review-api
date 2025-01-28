package controllers
import models.Movie
import services.DbService
import zio.ZIO
import zio.http.Body
import zio.http.Method
import zio.http.Request
import zio.http.Response
import zio.http.Routes
import zio.http.Status
import zio.http.handler
import zio.json.DecoderOps

class DbController(dbService: DbService) extends Controller {

  override def routes: Routes[Any, Nothing] = Routes(
    Method.GET / "movie" -> handler { (req: Request) =>
      if (req.hasJsonContentType) {
        Response(
          status = Status.BadRequest,
          body = Body.fromString("json is not ready yet")
        )
      } else {
        val name = req.queryParam("name")
        if (name.isDefined) {
          Response.json(s"${dbService.getMovie(name.get)}")
        } else {
          Response.text(s"please give movie title")
        }
      }
    },
    Method.GET / "description" -> handler { (req: Request) =>
      val name = req.queryParam("name")
      if (name.isDefined) {
        Response.json(s"description: ${dbService.getDescription(name.get)}")
      } else {
        Response.text(s"please give movie title")
      }
    },
    Method.POST / "movie" -> handler { (req: Request) =>
      val title = req.queryParam("title")
      if (title.isDefined) {
        val description = req.queryParamToOrElse("description", "")
        val link = req.queryParamToOrElse("link", "")
        dbService.createMovie(title.get, description, link)
        Response(
          status = Status.Created,
          body = Body.fromString(s"We added your movie ${title.get}!")
        )
      } else
        Response(
          status = Status.InternalServerError,
          body = Body.fromString("give the movie name plz")
        )
    },
    Method.DELETE / "movie" -> handler { (req: Request) =>
      val title = req.queryParam("title")
      if (title.isDefined) {
        dbService.deleteMovie(title.get)
        Response.text(s"movie $title deleted")
      } else
        Response.text(s"please give movie title")
    },
    Method.POST / "review" -> handler { (req: Request) =>
      val title = req.queryParam("title")
      if (title.isDefined) {
        dbService.deleteMovie(title.get)
        Response.text(s"movie $title deleted")
      } else
        Response.text(s"please give movie title")
    }
  )
}
