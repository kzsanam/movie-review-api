import scala.util.Try

import controllers.DbController
import controllers.HealthController
import services.FirestoreDbService
import zio._
import zio.http._
import zio.http.netty.NettyConfig
import zio.http.netty.NettyConfig.LeakDetectionLevel

object App extends ZIOAppDefault {
    // Set a port
    private val PORT = 58080

    // services
    private val dbService = {
        val db = new FirestoreDbService
        db.init()
        db
    }

    // controllers
    private val healthController = new HealthController
    private val dbController = new DbController(dbService)

    // routes
    private val healthRoutes = healthController.routes
    private val dbRoutes = dbController.routes

    // run stuff
    val run = ZIOAppArgs.getArgs.flatMap { args =>
        // Configure thread count using CLI
        val nThreads: Int = args.headOption.flatMap(x => Try(x.toInt).toOption).getOrElse(0)

        val config = Server.Config.default.port(PORT)
        val nettyConfig = NettyConfig.default
                .leakDetection(LeakDetectionLevel.PARANOID)
                .maxThreads(nThreads)
        val configLayer = ZLayer.succeed(config)
        val nettyConfigLayer = ZLayer.succeed(nettyConfig)

        (healthRoutes ++ dbRoutes)
                .serve[Any]
                .provide(configLayer, nettyConfigLayer, Server.customized)
    }
}
