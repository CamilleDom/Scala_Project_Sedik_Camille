// Classe pour un pays
case class Country(
  name: String,
  code: String,
  capital: String,
  continent: String,
  population: Long,
  area: Long,              // km²
  gdp: Double,             // PIB par habitant (USD)
  languages: List[String],
  currency: String
)

//Classes pour le résultat du chargement des pays
case class CountryLoadResult(
  goodFormat: List[Country],
  badCount: Int
)

case class ParStatistics(
    totalEntries: Int,
    validEntries: Int,
    invalidEntries: Int,
    duplicatesRemoved: Int
)

//Classe pour les statistiques des pays
case class CountryStats(
  totalCountries: Int,
  averagePopulation: Long,
  averageGdp: Double
)

case class TopCountry(
  name: String,
  continent: String,
  value: Double
)

case class LanguageStats(
  language: String,
  countryCount: Int,
  commonCountries: List[String]
)

case class PopulationDensity(
  name: String,
  continent: String,
  density: Double   // habitants / km²
)

case class GdpCategoryStats(
  category: String,     // High / Middle / Low income
  countryCount: Int
)

case class ContinentLanguageDiversity(
  continent: String,
  averageLanguagesPerCountry: Double
)

case class CapitalStats(
  continent: String,
  capitalCount: Int
)

case class CurrencyUsage(
  currency: String,
  countryCount: Int,
  countries: List[String]
)

case class ExtremeCountryStats(
  mostPopulated: String,
  leastPopulated: String,
  richest: String,
  poorest: String,
  largest: String,
  smallest: String
)

case class MultilingualCountry(
  name: String,
  languages: List[String]
)

// Classes pour le rapport final
case class StatCompile(
  countryStatistics: CountryStats,
  avPopByCont: Map[String, Double],
  top10ByPop: List[TopCountry],
  top10ByArea: List[TopCountry],
  top10ByGdp: List[TopCountry],
  top10ByDensity: List[TopCountry],
  top10ByWealth: List[TopCountry],
  countriesByContinent: Map[String, Int],
  multilingualCountries: List[MultilingualCountry],
  languageStatistics: List[LanguageStats],
  continentLangDiv: List[ContinentLanguageDiversity],
  popDensity: List[PopulationDensity],
  gdpCatStats: List[GdpCategoryStats],
  capStats: List[CapitalStats],
  curUsage: List[CurrencyUsage],
  extStats: ExtremeCountryStats
)

case class PerformanceStats(
  processingTimeMs: Long,
  entriesPerSecond: Double
)

case class CountryAnalysisReport(
    parser_Statistics: ParStatistics,

    statistics_Compile : StatCompile,

    performance_Metrics: PerformanceStats
)


