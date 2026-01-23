object Main extends App {

  println(" Final Project : Analyse de Counties\n")

/*  // Phase 1 Teste DataLoader
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
*/

  val filesList: List[String] = List("data/data_clean.json", "data/data_large.json", "data/data_dirty.json")

  filesList.foreach { fileName =>
    println(s"\n--- Processing file: $fileName ---")
    val result = for {
      //1. Charger les restaurants avec DataLoader
      countries <- DataLoader.loadCountry("data/data_dirty.json")
      _ = println(s" ${countries.length} countries charges")
      
      // 2. Valider et filtrer avec DataValidator
      validCountries = DataValidator.filterValid(countries)
      _ = println(s" ${validCountries.length} countries valides")
      
      /*// 3. Generer le rapport avec ReportGenerator
      report = ReportGenerator.generateReport(validCountries)
      _ = println(s" Rapport genere")
      */

      // 3. Generer le rapport avec ReportGenerator
      report = ReportGenerator.generateReport(
        parseStat = DataValidator.parserStatistics,
        statCompile = StatisticsCompiler.compileStatistics(validCountries),
        performance = PerformanceTracker.getPerformanceStats()
      )
      _ = println(s" Rapport genere")

      // 4. ecrire le rapport
      _ <- ReportGenerator.writeReport(report, s"results_$fileName.json")
      _ = println(s" Rapport ecrit dans results_$fileName.json")
      
    } yield report

    result match {
      case Right(report) =>
        ReportGenerator.writeFullReport(report, s"results_$fileName.txt")
        
      case Left(error) =>
        // TODO: Afficher l'erreur et quitter
        println(s" Erreur lors du pipeline ETL : $error")
    }
  }
}