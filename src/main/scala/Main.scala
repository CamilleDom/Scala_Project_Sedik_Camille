object Main extends App {

  println(" Final Project : Analyse de Counties\n")

  // Phase 1 Teste DataLoader
  val resultEither = DataLoader.loadCountry("data/data_large.json")

  resultEither.fold(
    
    errorMessage => println(s"Échec du chargement : $errorMessage"),

    
    countriesResult => {
      println("\n Loading Statistics")
      println("---------------------------")
      println(f"- Entrées valides           : ${countriesResult.goodFormat.size}")
      println(f"- Erreurs de parsing        : ${countriesResult.badCount}")

      
      val goodFormat: List[Country] = countriesResult.goodFormat
      println(s"${goodFormat.size} entrées valides prêts pour Filtration")

      // Phase 2 Teste DataValidator
      val validCountries = DataValidator.filterValid(goodFormat)
      val pStat = ParserStat(
        totalEntries = goodFormat.size + countriesResult.badCount,
        validEntries = validCountries.size,
        invalidEntries = (goodFormat.size - validCountries.size) + countriesResult.badCount,
        duplicatesRemoved = goodFormat.size - validCountries.size
      )
      println("\n Validation Statistics")
      println("---------------------------")
      println(f"- Entrees totales          : ${pStat.totalEntries}")
      println(f"- Entrees valides          : ${pStat.validEntries}")
      println(f"- Entrees invalides        : ${pStat.invalidEntries}")
      println(f"- Doublons supprimés       : ${pStat.duplicatesRemoved}")

    }
  )





/*
  // TODO: Creer le pipeline ETL avec for-comprehension
  val result = for {
    // TODO: 1. Charger les restaurants avec DataLoader
    countries <- DataLoader.loadCountry("data/data_dirty.json")
    _ = println(s" ${countries.length} countries charges")
    
    // TODO: 2. Valider et filtrer avec DataValidator
    validRestaurants = DataValidator.filterValid(restaurants)
    _ = println(s" ${validRestaurants.length} restaurants valides")
    
    // TODO: 3. Generer le rapport avec ReportGenerator
    report = ReportGenerator.generateReport(validRestaurants)
    _ = println(s" Rapport genere")
    
    // TODO: 4. ecrire le rapport
    _ <- ReportGenerator.writeReport(report, "results.json")
    _ = println(s" Rapport ecrit dans results.json")
    
  } yield report

  // TODO: Pattern matching sur result
  //   - Si Right(report) : Afficher les statistiques
  //   - Si Left(error) : Afficher l'erreur
  result match {
    case Right(report) =>
      println("\n STATISTIQUES")
      println("-------------")
      // TODO: Afficher report.statistics.totalRestaurants
      println(s"Total de restaurants : ${report.statistics.totalRestaurants}")
      // TODO: Afficher report.statistics.averageRating (avec f"${...}%.2f")
      println(f"Note moyenne : ${report.statistics.averageRating}%.2f")
      // TODO: Afficher report.statistics.vegetarianFriendlyCount
      println(s"Restaurants avec options vegetariennes : ${report.statistics.vegetarianFriendlyCount}")
      
      println("\n TOP 3 RESTAURANTS")
      println("-------------")
      // TODO: Afficher report.topRated avec zipWithIndex
      report.topRated.zipWithIndex.foreach { case (restaurant, index) =>
        println(f"${index + 1}%d. ${restaurant.name} - Note: ${restaurant.rating}%.2f")
      }
      
      println("\n PAR TYPE DE CUISINE")
      println("-------------")
      // TODO: Afficher report.byCuisine (trier par count decroissant)
      report.byCuisine.toList.sortBy(-_._2).foreach { case (cuisine, count) =>
        println(s"$cuisine : $count")
      }
      
      println("\n PAR GAMME DE PRIX")
      println("-------------")
      // TODO: Afficher report.byPriceRange
      report.byPriceRange.toList.sortBy(-_._2).foreach { case (priceRange, count) =>
        println(s"Gamme $priceRange : $count")
      }
      
      println("\n Pipeline ETL termine avec succès !")
      
    case Left(error) =>
      // TODO: Afficher l'erreur et quitter
      println(s" Erreur lors du pipeline ETL : $error")
  }*/
}