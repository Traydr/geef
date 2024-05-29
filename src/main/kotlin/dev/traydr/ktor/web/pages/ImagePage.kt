package dev.traydr.ktor.web.pages

import dev.traydr.ktor.utils.*
import dev.traydr.ktor.web.components.footer
import dev.traydr.ktor.web.components.header
import dev.traydr.ktor.web.components.navbar
import dev.traydr.ktor.web.components.wrapper
import kotlinx.html.*

fun HTML.imagePage() {
    attributes["data-theme"] = "dark"
    header("KT | Images", "Upload and download images")
    body {
        navbar()
        wrapper("Submitting Images") {
            div {
                classes = setOf("text-center", "gap-4")
                h2 {
                    classes = setOf("text-xl")
                    +"Here you will be uploading and downloading image files"
                }

                h4 {
                    +"Allowed extensions: png, jpg, jpeg"
                }
            }
            div {
                classes = setOf("container", "mx-auto", "grid", "grid-cols-2", "gap-4")
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
                            classes = setOf("btn btn-primary")
                            +"Upload"
                        }
                    }
                }
                div {
                    classes = setOf("flex", "justify-center")
                    form {
                        classes = setOf("grid", "grid-cols-1", "gap-1")
                        id = "download-image"
                        hxGet("/api/v1/download/{fileName}")
                        hxInclude("[name='fileName']")
                        hxExt("path-params")

                        input {
                            classes = setOf(
                                "input",
                                "input-bordered",
                                "input-accent",
                                "w-full",
                                "max-w-xs",
                                "grow"
                            )
                            placeholder = "filename.txt"
                            name = "fileName"
                            type = InputType.text
                        }

                        button {
                            classes = setOf("btn btn-primary")
                            +"Download"
                        }
                    }
                }
            }
        }
        footer()
    }
}