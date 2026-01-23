object DataValidator {

  // Valide un pays selon les règles
  def isValid(country: Country): Boolean = {
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

  // Filtre les pays valides et retourne le nombre + liste
  def filterValid(countries: List[Country]): (Int, List[Country]) = {
    val valid = countries.filter(isValid)
    (valid.size, valid)
  }

  // Supprime les doublons par code et retourne le nombre + liste
  def filterDoublons(countries: List[Country]): (Int, List[Country]) = {
    val deduped = countries.groupBy(_.code).map { case (_, list) => list.head }.toList
    (deduped.size, deduped)
  }

  // Parse et retourne les statistiques
  def parserEtStatistics(countries: List[Country], totalEntries: Int): (List[Country], ParStatistics) = {
    val (validCount, validCountries) = filterValid(countries)
    val (dedupCount, dedupCountries) = filterDoublons(validCountries)

    val parseStats = ParStatistics(
      totalEntries = totalEntries,
      validEntries = validCount,
      invalidEntries = totalEntries - validCount,
      duplicatesRemoved = validCount - dedupCount
    )

    (dedupCountries, parseStats)
  }
}
