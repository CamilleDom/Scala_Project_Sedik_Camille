object DataValidator {
  def isValid(country: Country): Boolean = {
    // TODO: Valider les champs selon les règles du README
    val hasPositivePopulation = country.population > 0
    val hasPositiveArea = country.area > 0
    val hasValidGdp = country.gdp >= 0
    val hasNonEmptyName = country.name.nonEmpty
    val hasNonEmptyCode = country.code.nonEmpty
    val hasNonEmptyCapital = country.capital.nonEmpty
    val hasNonEmptyContinent = country.continent.nonEmpty
    val hasNonEmptyCurrency = country.currency.nonEmpty
    val hasLanguages = country.languages.nonEmpty

    hasPositivePopulation && hasPositiveArea && hasValidGdp &&
    hasNonEmptyName && hasNonEmptyCode && hasNonEmptyCapital &&
    hasNonEmptyContinent && hasNonEmptyCurrency && hasLanguages
  }

  def filterValid(countries: List[Country]): List[Country] = {
    countries.filter(isValid)
  }

  def filterDoublons(countries: List[Country]): List[Country] = {
    countries.groupBy(_.code)
    .map { case (_, list) => list.head }
    .toList
  }

  def parserStatistics(countries: List[Country], size: Int): ParStatistics = {
    ParStatistics(
      totalEntries = size,
      validEntries = countries.length,
      invalidEntries = size - countries.length,
      duplicatesRemoved = size - countries.map(_.code).distinct.length
    )
  }
}