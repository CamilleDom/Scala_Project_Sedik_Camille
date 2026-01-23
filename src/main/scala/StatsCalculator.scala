object StatsCalculator {

  def StatCompile(countries:List[Country]): StatCompile = {

    StatCompile(
        globalStats = globalStats(countries),
        average_population_by_continent = average_population_by_continent(countries),
        top_10_by_population = top_10_by_population(countries),
        top_10_by_area = top_10_by_area(countries),
        top_10_by_gdp = top_10_by_gdp(countries),
        top_10_by_density = top_10_by_density(countries),
        top_10_by_wealth = top_10_by_wealth(countries),
        countries_by_continent = countries_by_continent(countries),
        multilingual_countries = multilingual_countries(countries),
        languageStats = languageStats(countries),
        continentLanguageDiversity = continentLanguageDiversity(countries),
        populationDensity = populationDensity(countries),
        gdpCategoryStats = gdpCategoryStats(countries),
        capitalStats = capitalStats(countries),
        currencyUsage = currencyUsage(countries),
        extremeStats = extremeStats(countries)
    )
  }

  def globalStats(countries:List[Country]): CountryStats = {

    CountryStats(
      totalCountries = countries.size,
      averagePopulation = if (countries.nonEmpty) countries.map(_.population).sum.toDouble / countries.size else 0.0,
      averageGdp = if (countries.nonEmpty) countries.map(_.gdp).sum.toDouble / countries.size else 0.0
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
        .map(c => TopCountry(c.name,c.continent,c.gdp.toDouble))
  }

  def top_10_by_density(countries:List[Country]): List[TopCountry] = {

    countries.map(c => (c, c.population.toDouble / c.area))
        .sortBy(-_._2)
        .take(10)
        .map{ case (c, density) => TopCountry(c.name, c.continent, density) }
  }

  def top_10_by_wealth(countries:List[Country]): List[TopCountry] = {

    countries.map(c => (c, c.gdp.toDouble / c.population))
        .sortBy(-_._2)
        .take(10)
        .map{ case (c, wealth) => TopCountry(c.name, c.continent, wealth) }
  }

  def countries_by_continent(countries:List[Country]): Map[String, Int] = {

    countries.groupBy(_.continent).mapValues(_.size).toMap
  }

  def average_population_by_continent(countries: List[Country]): Map[String, Double] = {
    countries.groupBy(_.continent)
        .map { case (continent, countriesInContinent) =>
        continent -> countriesInContinent.map(_.population).sum.toDouble / countriesInContinent.size
        }
  }

  def multilingual_countries(countries:List[Country]): List[MultilingualCountry] = {

    countries.sortBy(c => -c.languages.size)
         .map(c => MultilingualCountry(c.name , c.languages))

  }

  def languageStats(countries: List[Country]): List[LanguageStats] = {

    countries.flatMap(c => c.languages.map(lang => (lang, c)))
    .groupBy(_._1)
    .map { 
        case (lang, pairs) =>LanguageStats( lang, pairs.size, pairs.map(_._2)
            )
        }.toList
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
  
    countries.map { c =>
        val category = 
        if (c.gdp < 4000) "Low"
        else if (c.gdp <= 12000) "Middle"
        else "High"
        (category, c)
    }.groupBy(_._1).map {
        case (cat, pairs) => (cat, pairs.map(_._2))
    }.map { case (cat, countriesInCat) =>
        GdpCategoryStats(cat, countriesInCat, countriesInCat.size)
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