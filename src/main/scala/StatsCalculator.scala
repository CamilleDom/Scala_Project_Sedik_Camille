package miniEtl

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}

object DataLoader {

  /**
   * Lit un fichier JSON et parse les restaurants
   */
  def loadRestaurants(filename: String): Either[String, List[Restaurant]] = {
    // TODO: Utiliser Try pour lire le fichier
    //   1. Créer un Source.fromFile(filename)
    val source = Source.fromFile(filename)
    //   2. Lire le contenu avec source.mkString
    val content = source.mkString
    //   3. Fermer le fichier avec source.close() - IMPORTANT !
    source.close()
    //   4. Parser avec decode[List[Restaurant]](content)
    //   5. Gérer les erreurs avec pattern matching
    decode[List[Restaurant]](content) match {
      case Right(restaurants) => Right(restaurants)
      case Left(error) => Left(s"Erreur de parsing JSON: ${error.getMessage}")
    }
    
  }
}