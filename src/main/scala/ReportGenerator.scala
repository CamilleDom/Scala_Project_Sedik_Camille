import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try
import java.io.PrintWriter

object ReportGenerator {

  def generateReport(parseStat: ParStatistics, statCompile: StatCompile, performance: PerformanceStats): CountryAnalysisReport = {
    CountryAnalysisReport(
      parser_Statistics = parseStat,
      statistics_Compile = statCompile,
      performance_Metrics = performance
    )
  }

  def writeResult(report: CountryAnalysisReport, fileName: String): Either[String, Unit] = {
    Try {
      val json = report.asJson
      Files.write(
        Paths.get(s"output/results_$fileName.json"),
        json.spaces2.getBytes(StandardCharsets.UTF_8)
      )
    }.toEither.left.map(_.getMessage).map(_ => ())
  }
  
  def writeFullReport(report: CountryAnalysisReport, fileName: String): Unit = {
    val pw = new PrintWriter(s"output/report_$fileName.txt")
    try {
        pw.println(
        s"""=============================================
            |     RAPPORT D'ANALYSE - PAYS DU MONDE
            |=============================================
            |
            |📊 STATISTIQUES DE PARSING
            |---------------------------
            |- Entrées totales lues      : ${report.parser_Statistics.totalEntries}
            |- Entrées valides           : ${report.parser_Statistics.validEntries}
            |- Erreurs de parsing        : ${report.parser_Statistics.invalidEntries}
            |- Doublons supprimés        : ${report.parser_Statistics.duplicatesRemoved}
            |
            |📌 STATISTIQUES GLOBALES
            |-----------------------
            |- Nombre total de pays      : ${report.statistics_Compile.countryStatistics.totalCountries}
            |- Population moyenne        : ${report.statistics_Compile.countryStatistics.averagePopulation} hab.
            |- PIB moyen                 : ${report.statistics_Compile.countryStatistics.averageGdp} USD/hab
            |
            |🌍 RÉPARTITION PAR CONTINENT
            |-----------------------------
            |${report.statistics_Compile.countriesByContinent
            .map { case (c, n) => f"- $c%-25s : $n%d pays" }
            .mkString("\n")}
            |
            |📈 POPULATION MOYENNE PAR CONTINENT
            |----------------------------------
            |${report.statistics_Compile.avPopByCont
            .map { case (c, v) => f"- $c%-25s : ${v}%,.0f hab." }
            .mkString("\n")}
            |
            |👥 TOP 10 - POPULATION
            |----------------------
            |${report.statistics_Compile.top10ByPop.zipWithIndex
            .map { case (c, i) => f"${i + 1}%2d. ${c.name}%-25s : ${c.value}%,.0f hab." }
            .mkString("\n")}
            |
            |🗺️  TOP 10 - SUPERFICIE
            |-----------------------
            |${report.statistics_Compile.top10ByArea.zipWithIndex
            .map { case (c, i) => f"${i + 1}%2d. ${c.name}%-25s : ${c.value}%,.0f km²" }
            .mkString("\n")}
            |
            |💰 TOP 10 - PIB
            |---------------
            |${report.statistics_Compile.top10ByGdp.zipWithIndex
            .map { case (c, i) => f"${i + 1}%2d. ${c.name}%-25s : ${c.value}%,.2f USD/hab" }
            .mkString("\n")}
            |
            |🌐 TOP 10 - DENSITÉ
            |-------------------
            |${report.statistics_Compile.top10ByDensity.zipWithIndex
            .map { case (c, i) => f"${i + 1}%2d. ${c.name}%-25s : ${c.value}%,.2f hab/km²" }
            .mkString("\n")}
            |
            |💸 TOP 10 - RICHESSE
            |-------------------
            |${report.statistics_Compile.top10ByWealth.zipWithIndex
            .map { case (c, i) => f"${i + 1}%2d. ${c.name}%-25s : ${c.value}%,.2f USD/hab" }
            .mkString("\n")}
            |
            |🗣️  PAYS MULTILINGUES
            |--------------------
            |${report.statistics_Compile.multilingualCountries
            .map(c => s"- ${c.name} (${c.languages.mkString(", ")})")
            .mkString("\n")}
            |
            |🌐 STATISTIQUES SUR LES LANGUES
            |------------------------------
            |${report.statistics_Compile.languageStatistics
            .map(l => s"- ${l.language} : ${l.countryCount} pays (${l.commonCountries.mkString(", ")})")
            .mkString("\n")}
            |
            |🌍 DIVERSITÉ LINGUISTIQUE PAR CONTINENT
            |-------------------------------------
            |${report.statistics_Compile.continentLangDiv
            .map(c => f"- ${c.continent}%-20s : ${c.averageLanguagesPerCountry}%.2f langues/pays")
            .mkString("\n")}
            |
            |🧮 DENSITÉ DE POPULATION (TOUS PAYS)
            |----------------------------------
            |${report.statistics_Compile.popDensity
            .map(d => f"- ${d.name}%-25s (${d.continent}) : ${d.density}%,.2f hab/km²")
            .mkString("\n")}
            |
            |💰 CATÉGORIES DE PIB
            |-------------------
            |${report.statistics_Compile.gdpCatStats
            .map(g => f"- ${g.category}%-15s : ${g.countryCount}%d pays")
            .mkString("\n")}
            |
            |🏛️  NOMBRE DE CAPITALES PAR CONTINENT
            |-----------------------------------
            |${report.statistics_Compile.capStats
            .map(c => f"- ${c.continent}%-20s : ${c.capitalCount}%d capitales")
            .mkString("\n")}
            |
            |💱 UTILISATION DES MONNAIES
            |--------------------------
            |${report.statistics_Compile.curUsage
            .map(c => s"- ${c.currency} : ${c.countryCount} pays (${c.countries.mkString(", ")})")
            .mkString("\n")}
            |
            |🌏 EXTRÊMES PAR PAYS
            |-------------------
            |- Pays le plus peuplé      : ${report.statistics_Compile.extStats.mostPopulated}
            |- Pays le moins peuplé     : ${report.statistics_Compile.extStats.leastPopulated}
            |- Plus grande superficie   : ${report.statistics_Compile.extStats.largest}
            |- Plus petite superficie   : ${report.statistics_Compile.extStats.smallest}
            |- PIB le plus élevé        : ${report.statistics_Compile.extStats.richest}
            |- PIB le plus faible       : ${report.statistics_Compile.extStats.poorest}
            |
            |⏱️  PERFORMANCE
            |---------------
            |- Temps de traitement       : ${report.performance_Metrics.processingTimeMs} ms
            |- Entrées / seconde         : ${report.performance_Metrics.entriesPerSecond}
            |
            |=============================================
            |""".stripMargin
        )
    } finally {
        pw.close()
    }
  }

}
