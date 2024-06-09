package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.HTML
import kotlinx.html.body

fun HTML.profilePage(isOwnProfile: Boolean = false) {
    attributes["data-theme"] = "dark"
    header("GEEF | Profile", "Profile")
    body {
        navbar()
        wrapper("Profile", hideTitle = true) {
        }
        footer()
    }
}