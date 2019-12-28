package LunchApp

import scala.swing._
import swing.event._
import scala.io.Source
import java.io.PrintWriter
import java.awt.Color._

object MenuApp extends SimpleSwingApplication {

  //Getting the menus

  case class RestaurantMenu(
      val restaurantName: String,
      var menuDescription: String
  )

  val resNames = Array(
    "A Bloc",
    "Alvari",
    "Arvo",
    "Dipoli",
    "Konetekniikka",
    "Kvarkki",
    "Täffä",
    "Tietotekniikkatalo",
    "Tuas",
    "Valimo"
  )

  var language: String = {
    Source.fromFile("language.txt").getLines().mkString match {
      case "FI" => "FI"
      case "EN" => "EN"
      case _    => "FI" //Default language is Finnish
    }
  }

  //Using MenuFinders' getMenus and the Url object to get the menus off the restaurants' websites

  val menuDescriptions = {
    if (language == "FI")
      Array.tabulate(resNames.length)(
        i =>
          MenuFinder
            .getMenus(Url.finnishUrls(i)._1, Url.finnishUrls(i)._2, "fi")
      )
    else
      Array.tabulate(resNames.length)(
        i =>
          MenuFinder
            .getMenus(Url.englishUrls(i)._1, Url.englishUrls(i)._2, "en")
      )
  }

  val allMenus = Vector[RestaurantMenu]().toBuffer

  for (i <- 0 until resNames.length) {
    allMenus += new RestaurantMenu(resNames(i), menuDescriptions(i))
  }

  var favouriteRestaurant: Option[String] = Some(
    Source.fromFile("favRes.txt").getLines().mkString
  )

  //GUI

  def top = new MainFrame {
    title = "LunchApp"

    /*
     * I list every button as a different val so I can reference them later in a listenTo clause to make the menus update.
     * This might not be the best way to do it but it's the only way I found that works.
     */

    val button = Button("Refresh") {
      if (language == "FI")
        allMenus.map(
          i =>
            i.menuDescription = MenuFinder.getMenus(
              Url.finnishUrls(allMenus.indexOf(i))._1,
              Url.finnishUrls(allMenus.indexOf(i))._2,
              "fi"
            )
        )
      else
        allMenus.map(
          i =>
            i.menuDescription = MenuFinder.getMenus(
              Url.englishUrls(allMenus.indexOf(i))._1,
              Url.englishUrls(allMenus.indexOf(i))._2,
              "en"
            )
        )
    }

    val gluten = new MenuItem(Action("Lactose free") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFilter.filterAllergens("L", i.menuDescription)
      )
    })

    val lactose = new MenuItem(Action("Gluten free") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFilter.filterAllergens("G", i.menuDescription)
      )
    })

    val milk = new MenuItem(Action("Milk free") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFilter.filterAllergens("M", i.menuDescription)
      )
    })

    val vegan = new MenuItem(Action("Vegan") {
      allMenus.map(
        i =>
          i.menuDescription =
            MenuFilter.filterAllergens("Veg", i.menuDescription)
      )
    })

    val allergens = new MenuItem(Action("Contains allergens") {
      allMenus.map(
        i =>
          i.menuDescription =
            MenuFilter.filterNotAllergens("A", i.menuDescription)
      )
    })

    val chicken = new MenuItem(Action("Chicken") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFilter
            .filterFoodType(Set("broiler", "chicken"), i.menuDescription)
      )
    })

    val fish = new MenuItem(Action("Fish") {
      if (language == "FI")
        allMenus.map(
          i =>
            i.menuDescription = MenuFilter.filterFoodType(
              Set(
                "kala",
                "lohi",
                "lohta",
                "fish",
                "kampela",
                "kirjolohta",
                "kirjolohi",
                "ahven",
                "ahventa",
                "silakka",
                "siika",
                "kuha"
              ),
              i.menuDescription
            )
        )
      else
        allMenus.map(
          i =>
            i.menuDescription = MenuFilter.filterFoodType(
              Set("fish", "salmon", "tuna", "flounder", "trout", "cod"),
              i.menuDescription
            )
        )
    })

    val pasta = new MenuItem(Action("Pasta") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFilter
            .filterFoodType(Set("pasta", "spaghetti"), i.menuDescription)
      )
    })

    val finnish = new MenuItem(Action("Finnish") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFinder.getMenus(
            Url.finnishUrls(allMenus.indexOf(i))._1,
            Url.finnishUrls(allMenus.indexOf(i))._2,
            "fi"
          )
      )
      language = "FI"
      val pw = new PrintWriter("language.txt")
      pw.println("FI")
      pw.close()

    })

    val english = new MenuItem(Action("English") {
      allMenus.map(
        i =>
          i.menuDescription = MenuFinder.getMenus(
            Url.englishUrls(allMenus.indexOf(i))._1,
            Url.englishUrls(allMenus.indexOf(i))._2,
            "en"
          )
      )
      val pw = new PrintWriter("language.txt")
      language = "EN"
      pw.println("EN")
      pw.close()
    })

    //Displaying the menus in a GridPanel

    contents = new ScrollPane(new GridPanel(2, 5) {
      for (m <- allMenus) {
        contents += new TextArea(m.menuDescription) {
          editable = false
          border = new javax.swing.border.LineBorder(BLACK)
          font = new Font("Calibri", 0, 14)
          if (favouriteRestaurant == Some(m.restaurantName)) {
            background = PINK
          }
          listenTo(
            button,
            gluten,
            lactose,
            milk,
            vegan,
            allergens,
            chicken,
            fish,
            pasta,
            finnish,
            english
          )
          reactions += {
            case ButtonClicked(_) => text = m.menuDescription
          }
        }
      }
    })

    //The rest of the buttons

    menuBar = new MenuBar {
      contents += new Menu("File") {
        borderPainted = true
        contents += new MenuItem(Action("Exit") {
          sys.exit()
        })
      }
      contents += new Menu("FilterDiets") {
        borderPainted = true
        contents += gluten
        contents += lactose
        contents += milk
        contents += vegan
        contents += allergens
      }
      contents += new Menu("FilterFoodType") {
        borderPainted = true
        contents += chicken
        contents += fish
        contents += pasta
      }
      contents += new Menu("Favourite Restaurant") {
        borderPainted = true
        for (i <- resNames) {
          contents += new MenuItem(Action(i) {
            favouriteRestaurant = Some(i)
            val pw = new PrintWriter("favRes.txt")
            pw.println(i)
            pw.close()
            favouriteRestaurant = favouriteRestaurant
          })
        }
      }
      contents += new Menu("Language") {
        contents += finnish
        contents += english
      }
      contents += button
    }
    size = new Dimension(1920, 1080)
    centerOnScreen
  }

}
