package dev.traydr.geef.web.pages

import dev.traydr.geef.utils.*
import dev.traydr.geef.web.components.footer
import dev.traydr.geef.web.components.header
import dev.traydr.geef.web.components.navbar
import dev.traydr.geef.web.components.wrapper
import kotlinx.html.*

fun HTML.profilePage(uuid: String, isOwnProfile: Boolean = false) {
    attributes["data-theme"] = "dark"
    header("GEEF | Profile", "Profile")
    body {
        navbar()
        wrapper("Profile", hideTitle = true) {
            div {
                classes = setOf("grid", "justify-items-center", "grid-cols-1", "gap-4")
                if (isOwnProfile) {
                    div {
                        classes = setOf("flex", "justify-center")
                        form {
                            classes = setOf("grid", "grid-cols-1", "gap-1")
                            id = "upload-image"
                            hxPost("/api/v1/upload")
                            hxEncoding("multipart/form-data")

                            input {
                                classes = setOf(
                                    "file-input",
                                    "file-input-bordered",
                                    "file-input-accent",
                                    "w-full",
                                    "max-w-xs",
                                    "grow"
                                )
                                name = "file"
                                type = InputType.file
                            }

                            button {
                                classes = setOf("btn", "btn-primary")
                                +"Upload"
                            }
                        }
                    }
                }
            }
            div {
                classes = setOf("grid", "justify-items-center", "grid-cols-4", "gap-4")
                div {
                    id="images"
                    hxGet("/api/v1/profile/${uuid}/images")
                    hxSwap("innerHTML")
                    hxTarget("#images")
                }
            }
            footer()
        }
    }
}