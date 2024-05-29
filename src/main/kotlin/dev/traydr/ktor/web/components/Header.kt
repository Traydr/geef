package dev.traydr.ktor.web.components

import kotlinx.html.HTML
import kotlinx.html.*

fun HTML.header(title: String, description: String) {
    head {
        // Meta tags
        meta { charset = "UTF-8" }
        title { +title }
        meta {
            name = "description"
            content = description
        }
        meta {
            name = "viewport"
            content = "width=devide-width, initial-scale=1.0"
        }

        // Favicon
        link {
            rel = "icon"
            href = "/favicon.ico"
            sizes = "32x32"
        }
        link {
            rel="apple-touch-icon"
            href = "/apple-touch-icon.png"
        }
        link {
            rel="manifest"
            href = "/site.webmanifest"
        }

        // Css and js libs
        link {
            href = "/css"
            rel = "stylesheet"
            type = "text/css"
        }
        script {
            src = "https://unpkg.com/htmx.org@1.9.11"
            integrity = "sha384-0gxUXCCR8yv9FM2b+U3FDbsKthCI66oH5IA9fHppQq9DDMHuMauqq1ZHBpJxQ0J0"
            attributes["crossorigin"] = "anonymous"
        }
        script {
            src = "https://unpkg.com/htmx.org@1.9.12/dist/ext/path-params.js"
        }
    }
}