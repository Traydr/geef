package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.*
import kotlinx.html.*

fun HTML.indexPage() {
    attributes["data-theme"] = "dark"
    header("KT | Home", "The home page")
    body {
        navbar()
        wrapper("Home") {
            p {
                classes = setOf("text-center")
                +"I made this website to show the capabilities of Ktor and how other kotlin libraries interact with it"
                br { }
                +"This is my first project using kotlin and ktor, so expect that some things are not optimal, and that I may not be doing them correctly"
            }
            p {
                classes = setOf("text-center")
                +"You may view the source code through the link below"
            }
        }
        footer()
    }
}