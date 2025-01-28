import zio._
import zio.http._

object ZioClient extends ZIOAppDefault {

    val app =
        for {
            client   <- ZIO.serviceWith[Client](_.host("localhost").port(58080))
            request  =  Request.get("foo")//.addQueryParam("name", "John")
            response <- client.batched(request)
            _        <- response.body.asString.debug("Response")
        } yield ()

    def run = app.provide(Client.default)
}