package LunchApp

object MenuFilter {

  /**
    * Filters the given menu based on different diets (Gluten free, vegan, lactose free etc).
    * @param allergens a character representing a specific diet, eg "G" for gluten free or "L" for lactose free
    * @param menu the menu
    * @return the same menu but filtered according to the diets
    */
  def filterDiets(diets: String, menu: String): String = {
    val title = menu.split("\n").head
    val lines = menu.split("\n").toBuffer.tail
    val allergenInfo = lines.map(_.dropWhile(_ != '('))

    for (i <- 0 until allergenInfo.length) {
      if (!allergenInfo(i).contains(diets) && !lines(i).endsWith(":")) {
        lines(i) = ""
      }
    }
    title + "\n" + lines.mkString("\n")
  }

  /**
    * Filters the given menu as to only include entries that dont contain allergens
    * @param menu the menu
    * @return the same menu but filtered as described
    */
  def filterAllergens(menu: String): String = {
    val title = menu.split("\n").head
    val lines = menu.split("\n").toBuffer.tail
    val allergenInfo = lines.map(_.dropWhile(_ != '('))

    for (i <- 0 until allergenInfo.length) {
      if (allergenInfo(i).contains("A") && !lines(i).endsWith(":")) {
        lines(i) = ""
      }
    }
    title + "\n" + lines.mkString("\n")
  }

  /**
    * Filters the given menu based on some specific food types
    * @param foodType a set containing different food types to filter by
    * @param menu the menu
    * @return the same menu but filtered according to the food types
    */
  def filterFoodType(foodType: Set[String], menu: String): String = {
    val title = menu.split("\n").head
    val lines = menu.split("\n").toBuffer.tail
    val menuInfo = lines.map(_.takeWhile(_ != '('))

    for (i <- 0 until menuInfo.length) {
      if (!foodType.exists(menuInfo(i).toLowerCase().contains) && !lines(i)
            .endsWith(":")) {
        lines(i) = ""

      }
    }
    title + "\n" + lines.mkString("\n")
  }

}
