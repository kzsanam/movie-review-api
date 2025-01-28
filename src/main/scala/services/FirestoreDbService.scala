package services

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.{Firestore, FirestoreOptions}
import models.Movie
import scala.jdk.CollectionConverters.MapHasAsJava
import zio.json.EncoderOps

class FirestoreDbService extends DbService {
    var db: Option[Firestore] = None

    override def init(): Unit = {
        val firestoreOptions = FirestoreOptions
                .getDefaultInstance
                .toBuilder
                .setCredentials(GoogleCredentials.getApplicationDefault)
                .build

        db = Option(firestoreOptions.getService)
    }

    override def getMovie(movie: String): String = {
        val doc = db.get.collection("movies").whereEqualTo("title", movie)
                .get()
                .get()
                .getDocuments
                .get(0)

        Movie(
            doc.get("title").toString,
            doc.get("description").toString,
            doc.get("link").toString
        ).toJson
    }

    override def getDescription(movie: String): String = {
        db.get.collection("movies").whereEqualTo("title", movie)
                .get()
                .get()
                .getDocuments
                .get(0)
                .get("description")
                .toString
    }

    override def createMovie(title: String, description: String = "", link: String = ""): Unit = {
        db.get.collection("movies").document()
                .create(
                    Map(
                        "title" -> title,
                        "description" -> description,
                        "link" -> link
                    ).asJava)
                .get()
                .getUpdateTime
    }

    override def deleteMovie(movie: String): Unit = {
        val id = db.get.collection("movies").whereEqualTo("title", movie)
                .get()
                .get()
                .getDocuments
                .get(0).getId

        db.get
                .collection("movies")
                .document(id)
                .delete()
                .get()
    }
}
