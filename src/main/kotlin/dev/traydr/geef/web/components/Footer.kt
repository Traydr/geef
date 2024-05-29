package dev.traydr.geef.web.components

import kotlinx.html.FlowContent
import kotlinx.html.*

fun FlowContent.footer() = footer {
    classes = setOf(
        "footer",
        "footer-center",
        "p-10",
        "bg-base-100/75",
        "text-xl",
        "outline",
        "outline-offset-0",
        "outline-blue-800"
    )
    aside {
        classes = setOf()
        p {
            classes = setOf("font-bold")
            +"Made by Traydr"
        }
    }

    nav {
        div {
            classes = setOf("grid", "grid-flow-col", "gap-4")
            a {
                href = "https://github.com/Traydr/ktor-example"
                img {
                    classes = setOf("")
                    src = "/logos/github-mark-white.svg"
                    alt = "Github"
                    width = "32"
                    height = "32"
                }
            }
        }
    }
}