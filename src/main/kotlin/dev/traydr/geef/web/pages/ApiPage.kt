package dev.traydr.geef.web.pages

import dev.traydr.geef.utils.hxGet
import dev.traydr.geef.utils.hxSwap
import dev.traydr.geef.utils.hxTarget
import dev.traydr.geef.web.components.*
import kotlinx.html.*

fun HTML.ApiPage() {
    attributes["data-theme"] = "dark"
    header("KT | API", "Calling an external API")
    body {
        navbar()
        wrapper("External Weather API") {
            div {
                classes = setOf("grid", "grid-cols-1", "p-10")
                form {
                    classes = setOf("flex", "flex-col", "gap-2")
                    hxGet("/api/v1/ext")
                    hxTarget("#weather-output")
                    hxSwap("innerHTML")

                    label {
                        classes =
                            setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
                        +"Location"
                        input {
                            name = "location"
                            id = "location-input"
                            type = InputType.text
                            classes = setOf("grow")
                        }
                    }

                    button {
                        classes = setOf("btn", "btn-primary")
                        +"Get"
                    }
                }
                div {
                    id = "weather-output"
                    +"Output will be displayed here"
                }
            }
        }
        footer()
    }
}