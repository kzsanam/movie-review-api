package models

import zio.json.{DeriveJsonCodec, JsonCodec}

final case class Movie(
    title: String,
    description: String,
    link: String
)

object Movie {
  // Derive the encoder and decoder for the case class
  implicit val codec: JsonCodec[Movie] = DeriveJsonCodec.gen[Movie]
}
