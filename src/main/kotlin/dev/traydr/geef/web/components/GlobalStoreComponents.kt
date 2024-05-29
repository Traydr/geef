package dev.traydr.geef.web.components

import dev.traydr.geef.utils.*
import kotlinx.html.*

fun FlowContent.gsFormPost() = div {
    form {
        classes = setOf("flex", "flex-col", "gap-2")
        hxPost("/api/v1/global")

        label {
            classes =
                setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
            +"Key"
            input {
                name = "key"
                id = "input-key"
                type = InputType.text
                classes = setOf("grow")
            }
        }

        label {
            classes =
                setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
            +"Value"
            input {
                name = "value"
                id = "input-value"
                type = InputType.text
                classes = setOf("grow")
            }
        }

        button {
            classes = setOf("btn", "btn-primary", "gap-2")
            +"Submit"
        }
    }
}

fun FlowContent.gsFormPut() = div {
    form {
        classes = setOf("flex", "flex-col", "gap-2")
        hxPut("/api/v1/global")

        label {
            classes =
                setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
            +"Key"
            input {
                name = "key"
                id = "existing-key"
                type = InputType.text
                classes = setOf("grow")
            }
        }

        label {
            classes =
                setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
            +"Value"
            input {
                name = "value"
                id = "new-value"
                type = InputType.text
                classes = setOf("grow")
            }
        }

        button {
            classes = setOf("btn btn-primary")
            +"Update"
        }
    }
}

fun FlowContent.gsFormGet() = div {
    form {
        classes = setOf("flex", "flex-col", "gap-2")
        hxGet("/api/v1/global")
        hxTarget("#gs-output")
        hxSwap("innerHTML")

        label {
            classes =
                setOf("input", "input-bordered", "flex", "items-center", "input-accent", "gap-2")
            +"Key"
            input {
                name = "key"
                id = "output-key"
                type = InputType.text
                classes = setOf("grow")
            }
        }

        button {
            classes = setOf("btn", "btn-primary")
            +"Display"
        }

        div {
            id = "gs-output"
            role = "alert"
            classes = setOf(
                "alert",
                "alert-info"
            )
            +"Output will be displayed here"
        }
    }
}