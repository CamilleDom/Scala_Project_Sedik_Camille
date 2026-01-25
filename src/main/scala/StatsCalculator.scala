object StatsCalculator {

  def stat(countries:List[Country]): StatCompile = {

    StatCompile(
        countryStatistics = globalStats(countries),
        avPopByCont = average_population_by_continent(countries),
        top10ByPop = top_10_by_population(countries),
        top10ByArea = top_10_by_area(countries),
        top10ByGdp = top_10_by_gdp(countries),
        top10ByDensity = top_10_by_density(countries),
        top10ByWealth = top_10_by_wealth(countries),
        countriesByContinent = countries_by_continent(countries),
        multilingualCountries = multilingual_countries(countries),
        languageStatistics = languageStats(countries),
        continentLangDiv = continentLanguageDiversity(countries),
        popDensity = populationDensity(countries),
        gdpCatStats = gdpCategoryStats(countries),
        capStats = capitalStats(countries),
        curUsage = currencyUsage(countries),
        extStats = extremeStats(countries)
    )
  }

  def globalStats(countries:List[Country]): CountryStats = {

    CountryStats(
    totalCountries = countries.size,
    averagePopulation = if (countries.nonEmpty) countries.map(_.population).sum / countries.size.toLong else 0L,
    averageGdp = if (countries.nonEmpty) countries.map(_.gdp).sum / countries.size else 0.0
    ) 
  }

  def top_10_by_population(countries:List[Country]): List[TopCountry] = {

    countries
      .sortBy(-_.population)
      .take(10)
      .map(c => TopCountry(c.name, c.continent, c.population.toDouble))
  }

  def top_10_by_area(countries:List[Country]): List[TopCountry] = {

    countries.sortBy(-_.area)
        .take(10)
        .map(c => TopCountry(c.name,c.continent, c.area.toDouble))
  }

  def top_10_by_gdp(countries:List[Country]): List[TopCountry] = {
    countries.sortBy(-_.gdp)
        .take(10)
        .map(c => TopCountry(c.name,c.continent,c.gdp))
  }

  def top_10_by_density(countries:List[Country]): List[TopCountry] = {

    countries.map(c => (c, c.population.toDouble / c.area))
        .sortBy(-_._2)
        .take(10)
        .map{ case (c, density) => TopCountry(c.name, c.continent, density) }
  }

  def top_10_by_wealth(countries:List[Country]): List[TopCountry] = {

    countries.map(c => (c, c.gdp * 1e9 / c.population)) // Vu que la valeur est en milliards de dollards
        .sortBy(-_._2)
        .take(10)
        .map{ case (c, wealth) => TopCountry(c.name, c.continent, wealth) }
  }

  def countries_by_continent(countries:List[Country]): Map[String, Int] = {

    countries.groupBy(_.continent).view.mapValues(_.size).toMap
  }

  def average_population_by_continent(countries: List[Country]): Map[String, Double] = {
    countries.groupBy(_.continent)
        .map { case (continent, countriesInContinent) =>
        continent -> countriesInContinent.map(_.population).sum.toDouble / countriesInContinent.size
        }
  }

  def multilingual_countries(countries:List[Country]): List[MultilingualCountry] = {

    countries.filter(_.languages.size >= 3)
      .sortBy(c => -c.languages.size)
      .map(c => MultilingualCountry(c.name , c.languages))


  }

  def languageStats(countries: List[Country]): List[LanguageStats] = {
    countries.flatMap(c => c.languages.map(lang => (lang, c.name))) // extraire le nom
      .groupBy(_._1)
      .map { case (lang, pairs) =>
        LanguageStats(
          language = lang,
          countryCount = pairs.size,
          commonCountries = pairs.map(_._2) // maintenant c'est List[String]
        )
      }.toList
      .sortBy(-_.countryCount).take(5) // inverse le tri par ordre décroissant
  }

  def continentLanguageDiversity(countries:List[Country]): List[ContinentLanguageDiversity] = {

    countries.groupBy(_.continent)
        .map{
            case(continent, countriesInC) => 
            ContinentLanguageDiversity( 
                continent,
                countriesInC.flatMap( _.languages).distinct.size.toDouble/countriesInC.size

            )
        }.toList
  }

  def populationDensity(countries:List[Country]): List[PopulationDensity] = {

    countries.map(c => PopulationDensity(c.name , c.continent, c.population.toDouble/c.area)).sortBy(_.density)
  }

  def gdpCategoryStats(countries: List[Country]): List[GdpCategoryStats] = {
    countries
      .map { c =>
        val category =
          if (c.gdp < 4000) "Low"
          else if (c.gdp <= 12000) "Middle"
          else "High"
        (category, c)
      }
      .groupBy(_._1)
      .map { case (cat, pairs) =>
        GdpCategoryStats(
          category = cat,
          countryCount = pairs.size
        )
      }.toList
  }

  def capitalStats(countries:List[Country]): List[CapitalStats] = {

    countries.groupBy(_.continent)
        .map{
            case(continent,countriesInC) =>
            CapitalStats(continent, countriesInC.size)
        }.toList
  }

  def currencyUsage(countries:List[Country]): List[CurrencyUsage] = {

    countries.groupBy(_.currency)
        .map{
            case(currency,countriesWc) =>
            CurrencyUsage(currency, countriesWc.size , countriesWc.map(_.name))
        }.toList
  }

  def extremeStats(countries:List[Country]): ExtremeCountryStats = {

    ExtremeCountryStats(
        mostPopulated = countries.maxBy(_.population).name,
        leastPopulated = countries.minBy(_.population).name,
        richest = countries.maxBy(_.gdp).name,
        poorest = countries.minBy(_.gdp).name,
        largest = countries.maxBy(_.area).name,
        smallest = countries.minBy(_.area).name

    )
  }

}