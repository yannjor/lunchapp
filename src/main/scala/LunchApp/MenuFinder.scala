package LunchApp

import java.util.Calendar
import scala.io.Source
import java.text.SimpleDateFormat
import org.json4s._
import org.json4s.native.JsonMethods._
import collection.mutable.Buffer
import scala.collection.mutable

object MenuFinder {

  val now = Calendar.getInstance.getTime
  val date = new SimpleDateFormat("yyyy-MM-dd").format(now)

  /**
    * Retrieves the menus from the restaurant's website in JSON and parses it into a more readable format
    * @param url the restaurants url
    * @param restaurantName the name of the restaurant
    * @param language the desired language, "fi" for finnish and "en" for english
    * @return the menus for the day, or an error string in case something goes wrong
    */
  def getMenus(
      url: String,
      restaurantName: String,
      language: String
  ): String = {
    val tab = " " * 5
    if (url.contains("fazer")) {
      try {
        val jsonString = Source.fromURL(url).mkString
        val json = parse(jsonString)
        val menusForToday = ((json \ "MenusForDays")(0) \ "SetMenus" \ "Components")
        val menuNames = (json \ "MenusForDays")(0) \ "SetMenus" \ "Name"
        val nameList = menuNames.values.asInstanceOf[List[String]]
        val menuList = menusForToday.values
          .asInstanceOf[List[List[String]]]
          .map(_.map(_.replaceAll("\n", "")))
        val menuListFixed = menuList
          .map(_.map(_.replaceAll("\n", "")))
          .map(_.map(_.replaceAll("\t", ""))) //Removes extra tabs and line breaks

        val menu = nameList.zip(menuListFixed)

        var start = restaurantName + "\n\n"

        for (p <- menu) {
          p._1 match {
            case null =>
              start = start + p._2.mkString("\n") + "\n\n" //If there is no menu title
            case _ =>
              start = start + p._1 + ":" + "\n\n" + p._2.mkString("\n") + "\n\n"
          }
        }
        start.split("\n").map(tab + _).mkString("\n") + "\n"
      } catch {
        case e: Throwable =>
          tab + restaurantName + "\n\n" + "Error retrieving menus."
      }

    } else if (url.contains("sodexo")) {
      try {
        val jsonString = Source.fromURL(url).mkString
        val json = parse(jsonString)
        val menuList = mutable.ArrayBuffer[String]()
        for (i <- 1 to 5) {
          val entry = json \ "courses" \ i.toString() \ ("title_" + language)
          menuList += entry.values.toString()
        }
        val menuListFixed = menuList
          .map(_.replaceAll("\t", ""))
          .map(_.replaceAll("\n", "")) //Removes extra tabs and line breaks

        val properties = Buffer[String]()

        for (i <- 0 until menuListFixed.length) {
          json \ "courses" \ i.toString \ "properties" match {
            case JNothing => properties += ""
            case _ =>
              properties += (json \ "courses" \ i.toString \ "properties").values
                .asInstanceOf[String]
          }
        }

        val pairs = menuListFixed.zip(properties)
        var start = restaurantName + "\n\n"

        for (p <- pairs) {
          p._2 match {
            case "" => start = start + p._1 + "\n\n"
            case _  => start = start + p._1 + " (" + p._2 + ")" + "\n\n"
          }
        }

        start.split("\n").map(tab + _).mkString("\n") + "\n"
      } catch {
        case e: Throwable =>
          tab + restaurantName + "\n\n" + "Error retrieving menus."
      }

    } else {
      var taffaMenu = ""
      try {
        taffaMenu = {
          val taffa = Source.fromURL(url).mkString
          if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) != 1 && Calendar
                .getInstance()
                .get(Calendar.DAY_OF_WEEK) != 7) { //No menus on saturdays and sundays
            ("TÄFFÄ " + "\n" + taffa)
              .split("\n")
              .map(tab + _)
              .mkString("\n\n")
          } else
            tab + "TÄFFÄ\n\n" + "Restaurant closed."
        }
      } catch {
        case e: Throwable =>
          taffaMenu = tab + "TÄFFÄ\n\n" + "Error retrieving menus."
      }
      taffaMenu
    }
  }

}
