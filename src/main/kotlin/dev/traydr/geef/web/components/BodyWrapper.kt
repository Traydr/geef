package dev.traydr.geef.web.components

import kotlinx.html.*

fun FlowContent.wrapper(title: String, hideTitle: Boolean = false, block: DIV.() -> Unit) = div {
    classes = setOf(
        "flex",
        "flex-col",
        "min-h-screen",
    )

    if (!hideTitle) {
        h1 {
            classes = setOf("text-bold", "text-white", "text-center", "text-3xl", "p-3")
            +title
        }
    }

    block()
}