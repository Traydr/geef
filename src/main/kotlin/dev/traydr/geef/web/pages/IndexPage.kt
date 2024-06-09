package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.*
import kotlinx.html.*

fun HTML.indexPage() {
    attributes["data-theme"] = "dark"
    header("GEEF | Home", "Home page to Geef")
    body {
        navbar()
        wrapper("Home") {
            p {
                +"Enjoy Geef, a place where you can share your images"
            }
        }
        footer()
    }
}