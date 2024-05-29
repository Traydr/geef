package dev.traydr.geef.web.pages

import dev.traydr.geef.web.components.*
import kotlinx.html.*

fun HTML.databasePage() {
    attributes["data-theme"] = "dark"
    header("KT | Storage Manipulation", "Manipulating data in the database")
    body {
        navbar()
        wrapper("Global key-value store") {
            div {
                classes = setOf("container", "mx-auto", "grid", "grid-cols-2", "gap-4")

                gsFormTempl({ gsFormPost() }, "You will be submitting a key-value pair to a global store")
                gsFormTempl({ gsFormGet() }, "You will be retrieving a value from the global key-value store")
                gsFormTempl({ gsFormPut() }, "You will be updating a key-value pair to a global store")
            }
        }
        footer()
    }
}

fun FlowContent.gsFormTempl(unit: () -> Unit, title: String) = div {
    classes = setOf("flex", "justify-center", "grid", "grid-cols-1")

    h2 {
        classes = setOf("text-xl", "p-4", "text-center")
        +title
    }

    unit()
}