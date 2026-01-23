object Main extends App {

  println("Final Project : Analyse de Countries\n")

  val filesList: List[String] = List(
    "data_clean",
    "data_large",
    "data_dirty"
  )

  filesList.foreach { fileName =>
    val start = System.currentTimeMillis()

    println(s"\n--- Processing file: $fileName ---")

    val reportEither = for {
      // 1️- Charger les countries
      countryLoadResult <- DataLoader.loadCountry(fileName)  // Either[String, CountryLoadResult]

      // 2️- Extraire bonnes et mauvaises entrées
      goodFormat = countryLoadResult.goodFormat
      badCount  = countryLoadResult.badCount
      _ = println(s"${goodFormat.length} countries charges")

      // 3️- Valider et filtrer (enlever doublons + stats parsing)
      parsedTuple = DataValidator.parserEtStatistics(goodFormat, goodFormat.size + badCount)
      validCountries = parsedTuple._1
      parseStatistics = parsedTuple._2
      _ = println(s"${validCountries.length} countries valides")

      // 4️- Compiler statistiques
      compiledStats = StatsCalculator.stat(validCountries)

      // 5️- Calculer performance
      end = System.currentTimeMillis()
      duration = end - start
      performance = PerformanceStats(
        processingTimeMs = duration,
        entriesPerSecond = ((goodFormat.size + badCount).toDouble / duration) * 1000
      )
      _ = println(s"Temps de traitement : $duration ms")

      // 6️- Générer le rapport
      report = ReportGenerator.generateReport(
        parseStat = parseStatistics,
        statCompile = compiledStats,
        performance = performance
      )
      _ = println(s"Rapport genere")

      // 7️- Écrire le rapport JSON
      _ = ReportGenerator.writeResult(report, s"$fileName") match {
        case Right(_) => println(s"Rapport écrit dans results_$fileName.json")
        case Left(err) => println(s"Erreur écriture JSON : $err")
      }

      // 8️- Écrire le rapport texte
      _ = ReportGenerator.writeFullReport(report, s"$fileName")
      _ = println(s"Rapport texte écrit dans rapport_$fileName.txt")
    } yield report

    // Gestion d'erreur globale
    reportEither.left.foreach { error =>
      println(s"Erreur de chargement ou traitement du fichier $fileName : $error")
    }
  }
}
