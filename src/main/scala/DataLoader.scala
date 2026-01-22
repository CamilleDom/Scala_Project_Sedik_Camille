import io.circe._
import io.circe.parser._
import io.circe.generic.auto._
import scala.io.Source


object DataLoader {

  /** 
   * Charge les pays depuis un fichier JSON et retourne un Either :
   * - Left(error) si le parsing global échoue
   * - Right(CountriesLoadResult) sinon
   */
  def loadCountry(filePath: String): Either[String, CountryLoadResult] = {
    try {
      // Lire le fichier
      val content = Source.fromFile(filePath).mkString

      // Décoder en List[Json]
      decode[List[Json]](content) match {
        case Left(err) =>
          Left(s"Erreur globale parsing JSON: ${err.getMessage}")

        case Right(jsonList) =>
          // Décoder chaque Json individuellement
          val decoded: List[Either[Error, Country]] = jsonList.map(_.as[Country])

          // Séparer bonnes et mauvaises entrées
          val goodFormat = decoded.collect { case Right(c) => c }
          val badCount = decoded.collect { case Left(_) => 1 }.sum

          

          // Retourner le résultat
          Right(CountryLoadResult(goodFormat, badCount))
      }

    } catch {
      case ex: Exception =>
        Left(s"Erreur lors de la lecture du fichier: ${ex.getMessage}")
    }
  }
}
