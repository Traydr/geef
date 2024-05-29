package dev.traydr.ktor.web.pages

import com.openmeteo.api.Forecast
import com.openmeteo.api.OpenMeteo
import com.openmeteo.api.common.Response
import com.openmeteo.api.common.time.Timezone
import com.openmeteo.api.common.units.TemperatureUnit
import dev.traydr.ktor.utils.hxGet
import dev.traydr.ktor.utils.hxSwap
import dev.traydr.ktor.utils.hxTarget
import dev.traydr.ktor.web.components.*
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