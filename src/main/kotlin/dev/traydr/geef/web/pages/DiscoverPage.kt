package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.HTML
import kotlinx.html.body

fun HTML.discoverPage() {
    attributes["data-theme"] = "dark"
    header("GEEF | Discover", "Discover what other people are up to in Geef")
    body {
        navbar()
        wrapper("Discover") {
        }
        footer()
    }
}