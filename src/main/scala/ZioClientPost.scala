import models.Movie
import zio._
import zio.http._
import zio.json.EncoderOps

object ZioClientPost extends ZIOAppDefault {

  val movie = Movie(
    "title",
    "desc",
    "link"
  ).toJson

    private val app: ZIO[Client, Throwable, Unit] =
        for {
            client <- ZIO.serviceWith[Client](_.host("localhost").port(58080))
            request = Request.post(
                "http://localhost:58080/movie",
                Body.fromString(movie)
            )//.addQueryParam("name", "John")
            response <- client.batched(request)
            _        <- response.body.asString.debug("Response")
        } yield ()

    def run: ZIO[Any, Throwable, Unit] = app.provide(Client.default)
}