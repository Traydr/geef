package dev.traydr.geef.web.pages

import dev.traydr.geef.utils.hxPost
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
            form {
                classes = setOf("flex", "flex-col", "gap-2")
                hxPost("/api/v1/auth/login")

                label {
                    classes =
                        setOf(
                            "input",
                            "input-bordered",
                            "flex",
                            "items-center",
                            "input-accent",
                            "gap-2"
                        )
                    +"Email"
                    input {
                        name = "email"
                        id = "email"
                        type = InputType.text
                        classes = setOf("grow")
                    }
                }

                label {
                    classes =
                        setOf(
                            "input",
                            "input-bordered",
                            "flex",
                            "items-center",
                            "input-accent",
                            "gap-2"
                        )
                    +"Password"
                    input {
                        name = "password"
                        id = "password"
                        type = InputType.password
                        classes = setOf("grow")
                    }
                }

                button {
                    classes = setOf("btn", "btn-primary", "gap-2")
                    +"Login"
                }
            }
        }
        footer()
    }
}