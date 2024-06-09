package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.HTML
import kotlinx.html.body

fun HTML.newPostPage() {
    attributes["data-theme"] = "dark"
    header("GEEF | New Post", "New Post on Geef")
    body {
        navbar()
        wrapper("New Post") {
        }
        footer()
    }
}