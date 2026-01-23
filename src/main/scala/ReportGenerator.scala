
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.util.Try
import java.io.PrintWriter

object ReportGenerator {
    def generateReport( parseStat : ParStatistics, statCompile: StatCompile, performance: PerformanceStats): CountryAnalysisReport = {
        CountryAnalysisReport(
            parser_Statistics = parseStat,
            statistics_Compile = statCompile,
            performance_Metrics = performance
        )
    }

    def writeReport(report: CountryAnalysisReport, filename: String): Either[String, Unit] = {
    Try {
      val json = report.asJson
      Files.write(
        Paths.get(filename),
        json.spaces2.getBytes(StandardCharsets.UTF_8)
      )
    }.toEither.left.map(_.getMessage).map(_ => ()) 
    }

    def writeFullReport(report: CountryAnalysisReport, filePath: String): Unit = {
    val pw = new PrintWriter(filePath)

    try {
        pw.println(
        s"""=============================================
            |     RAPPORT D'ANALYSE - PAYS DU MONDE
            |=============================================
            |
            |📊 STATISTIQUES DE PARSING
            |---------------------------
            |- Entrées totales lues      : ${report.parser_Statistics.total}
            |- Entrées valides           : ${report.parser_Statistics.valid}
            |- Erreurs de parsing        : ${report.parser_Statistics.invalid}
            |- Doublons supprimés        : ${report.parser_Statistics.duplicates}
            |
            |🌍 RÉPARTITION PAR CONTINENT
            |-----------------------------
            |${report.statistics_Compile.countries_by_continent.map { case (continent, count) => f"- $continent%-25s : $count%d pays" }.mkString("\n")}
            |
            |👥 TOP 10 - POPULATION
            |----------------------
            |${report.statistics_Compile.top_10_by_population.zipWithIndex.map { case (c, i) => f"${i+1}%d. ${c.name}%-25s : ${c.value}%,d hab." }.mkString("\n")}
            |
            |🗺️  TOP 10 - SUPERFICIE
            |-----------------------
            |${report.statistics_Compile.top_10_by_area.zipWithIndex.map { case (c, i) => f"${i+1}%d. ${c.name}%-25s : ${c.value}%,d km²" }.mkString("\n")}
            |
            |💰 TOP 10 - PIB
            |---------------
            |${report.statistics_Compile.top_10_by_gdp.zipWithIndex.map { case (c, i) => f"${i+1}%d. ${c.name}%-25s : ${c.value}%,.2f milliards USD" }.mkString("\n")}
            |
            |🌐 TOP 10 - DENSITÉ
            |-------------------
            |${report.statistics_Compile.top_10_by_density.zipWithIndex.map { case (c, i) => f"${i+1}%d. ${c.name}%-25s : ${c.value}%,.2f hab/km²" }.mkString("\n")}
            |
            |💸 TOP 10 - RICHESSE
            |-------------------
            |${report.statistics_Compile.top_10_by_wealth.zipWithIndex.map { case (c, i) => f"${i+1}%d. ${c.name}%-25s : ${c.value}%,.2f USD/hab" }.mkString("\n")}
            |
            |🗣️  LANGUES LES PLUS RÉPANDUES
            |--------------------------------
            |${report.statistics_Compile.languageStats.zipWithIndex.map { case (l, i) => f"${i+1}%d. ${l.language}%-25s : ${l.countryCount}%d pays" }.mkString("\n")}
            |
            |📈 MOYENNES PAR CONTINENT
            |--------------------------
            |${report.statistics_Compile.average_population_by_continent.map { case (continent, avg) => f"- $continent%-25s : ${avg}%.0f hab. (moyenne)" }.mkString("\n")}
            |
            |🏛️  CAPITALS - STATISTIQUES
            |---------------------------
            |${report.statistics_Compile.capitalStats.map(s => f"- ${s.capital}%-25s : ${s.population}%,d hab.").mkString("\n")}
            |
            |💱 USAGE DES MONNAIES
            |----------------------
            |${report.statistics_Compile.currencyUsage.map(c => f"- ${c.currency}%-10s : ${c.countryCount} pays").mkString("\n")}
            |
            |📊 CATEGORIES DE PIB
            |-------------------
            |${report.statistics_Compile.gdpCategoryStats.map(g => f"- ${g.category}%-15s : ${g.countryCount} pays").mkString("\n")}
            |
            |🌏 DIVERSITÉ LINGUISTIQUE PAR CONTINENT
            |--------------------------------------
            |${report.statistics_Compile.continentLanguageDiversity.map(d => f"- ${d.continent}%-15s : ${d.averageLanguagesPerCountry} langues").mkString("\n")}
            |
            |🏘️ DENSITÉ DE POPULATION
            |-----------------------
            |${report.statistics_Compile.populationDensity.map(d => f"- ${d.name}%-25s : ${d.density}%,.2f hab/km²").mkString("\n")}
            |
            |📈 EXTRÊMES PAR PAYS
            |-------------------
            |${report.statistics_Compile.extremeStats match {
                case e =>
                s"- Pays le plus peuplé      : ${e.mostPopulated.name} (${e.mostPopulated.population} hab.)\n" +
                s"- Pays le moins peuplé     : ${e.leastPopulated.name} (${e.leastPopulated.population} hab.)\n" +
                s"- Plus grande superficie   : ${e.largestArea.name} (${e.largestArea.area} km²)\n" +
                s"- Plus petite superficie   : ${e.smallestArea.name} (${e.smallestArea.area} km²)\n" +
                s"- PIB le plus élevé         : ${e.highestGdp.name} (${e.highestGdp.gdp} milliards USD)\n" +
                s"- PIB le plus faible        : ${e.lowestGdp.name} (${e.lowestGdp.gdp} milliards USD)"
            }}
            |
            |⏱️  PERFORMANCE
            |---------------
            |- Temps de traitement       : ${report.performance_Metrics.processingTimeMs} ms
            |- Entrées/seconde           : ${report.performance_Metrics.entriesPerSecond}
            |
            |=============================================
            |""".stripMargin
        )
    } finally {
        pw.close()
    }
    }

}