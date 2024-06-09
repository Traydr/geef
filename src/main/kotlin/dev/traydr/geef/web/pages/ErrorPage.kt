package dev.traydr.geef.web.pages


import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.*

fun HTML.errorPage(text: String, httpError: Int) {
    attributes["data-theme"] = "dark"
    header("GEEF | Error $httpError", text)
    body {
        classes = setOf()
        navbar()
        wrapper(text) {

        }
        footer()
    }
}