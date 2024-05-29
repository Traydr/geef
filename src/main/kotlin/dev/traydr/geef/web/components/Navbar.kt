package dev.traydr.geef.web.components

import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.*

fun FlowContent.navbar() = nav {
    classes = setOf("navbar", "bg-base-100/75", "outline", "outline-offset-0", "outline-blue-800")

    div {
        classes = setOf("navbar-start")
        a {
            classes = setOf("btn", "btn-ghost", "text-xl", "text-bold")
            href = "/"
            +"Ktor Example"
        }
    }

    div {
        classes = setOf("navbar-center", "tabs", "tabs-boxed", "bg-sky-800")
        ul {
            classes = setOf("menu", "menu-horizontal", "px-1")
            li {
                a {
                    href = "/images"
                    +"Images"
                }
            }
            li {
                a {
                    href = "/db"
                    +"Db"
                }
            }
            li {
                a {
                    href = "/ext-api"
                    +"Api"
                }
            }
        }


    }

    div {
        classes = setOf("navbar-end", "text-xl")
        div {
            classes = setOf("pr-4")
            +"Welcome!"
        }
    }
}