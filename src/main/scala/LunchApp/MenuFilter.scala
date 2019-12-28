package LunchApp

object MenuFilter {

  /*
   * Used to filter menus based on different diets (Gluten free, vegan, lactose free etc).
   * These are always in a parenthesis after the menus so it's quite easy to filter them.
   */

  def filterAllergens(allergens: String, menu: String): String = {

    val title = menu.split("\n").head
    val lines = menu.split("\n").toBuffer.tail
    val allergenInfo = lines.map(_.dropWhile(_ != '('))

    for (i <- 0 until allergenInfo.length) {

      if (!allergenInfo(i).contains(allergens) && !lines(i).endsWith(":")) {
        lines(i) = ""
      }
    }
    title + "\n" + lines.mkString("\n")
  }

  //Same as filterAllergens just the "oppsite"

  def filterNotAllergens(allergens: String, menu: String): String = {

    val title = menu.split("\n").head
    val lines = menu.split("\n").toBuffer.tail
    val allergenInfo = lines.map(_.dropWhile(_ != '('))

    for (i <- 0 until allergenInfo.length) {

      if (allergenInfo(i).contains(allergens) && !lines(i).endsWith(":")) {
        lines(i) = ""
      }
    }
    title + "\n" + lines.mkString("\n")
  }

  //Used for filtering based on different food types. It takes a Set of different food types it wants to filter.

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
