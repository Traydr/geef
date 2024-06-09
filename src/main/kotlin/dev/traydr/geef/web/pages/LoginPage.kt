package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.*

fun HTML.loginPage() {
    attributes["data-theme"] = "dark"
    header("GEEF | Login", "Login in to Geef")
    body {
        navbar()
        wrapper("Login") {
        }
        footer()
    }
}