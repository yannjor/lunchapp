package LunchApp

object Url {
  
  
  private val year = MenuFinder.date.split("-")(0)
  private val month = MenuFinder.date.split("-")(1)
  private val day = MenuFinder.date.split("-")(2)   
  
  
  val finnishUrls = Vector[(String, String)](
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=3087&language=fi", "A BLOC "),
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=0190&language=fi", "ALVARI "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/39106/" + year + "/" + month + "/" + day + "/fi", "ARVO "),
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=3101&language=fi", "DIPOLI "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/140/" + year + "/" + month + "/" + day + "/fi", "KONETEKNIIKKA "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/26521/" + year + "/" + month + "/" + day + "/fi", "KVARKKI "),
    ("http://api.teknolog.fi/taffa/fi/today/", "TÄFFÄ"),    
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/142/" + year + "/" + month + "/" + day + "/fi", "TIETOTEKNIIKKATALO "),    
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=0199&language=fi", "TUAS "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/13918/" + year + "/" + month + "/" + day + "/fi", "VALIMO ")
  )
  
  val englishUrls = Vector[(String, String)](
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=3087&language=en", "A BLOC "),
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=0190&language=en", "ALVARI "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/39106/" + year + "/" + month + "/" + day + "/fi", "ARVO "),
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=3101&language=en", "DIPOLI "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/140/" + year + "/" + month + "/" + day + "/fi", "KONETEKNIIKKA "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/26521/" + year + "/" + month + "/" + day + "/fi", "KVARKKI "),
    ("http://api.teknolog.fi/taffa/en/today/", "TÄFFÄ"),    
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/142/" + year + "/" + month + "/" + day + "/fi", "TIETOTEKNIIKKATALO "),    
    ("https://www.fazerfoodco.fi/modules/json/json/Index?costNumber=0199&language=en", "TUAS "),
    ("https://www.sodexo.fi/ruokalistat/output/daily_json/13918/" + year + "/" + month + "/" + day + "/fi", "VALIMO ")
  )    
  
  
  
  
  
  
}