package dev.traydr.geef.web.pages

import dev.traydr.geef.domain.User
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.*
import org.koin.ktor.ext.inject

fun HTML.discoverPage(users: List<User>) {
    attributes["data-theme"] = "dark"
    header("GEEF | Discover", "Discover what other people are up to in Geef")
    body {
        navbar()
        wrapper("Discover") {
            div {
                classes = setOf("grid", "grid-cols-1", "gap-4")
                for (user in users) {
                    div {
                        a {
                            href = "/profile/${user.publicUUID}"
                            +user.username!!
                        }
                    }
                }
            }
        }
        footer()
    }
}