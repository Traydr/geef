package dev.traydr.geef.utils

import kotlinx.html.HTMLTag

fun HTMLTag.hxGet(value: String) {
    attributes += "hx-get" to value
}

fun HTMLTag.hxPost(value: String) {
    attributes += "hx-post" to value
}

fun HTMLTag.hxPut(value: String) {
    attributes += "hx-put" to value
}

fun HTMLTag.hxPatch(value: String) {
    attributes += "hx-patch" to value
}

fun HTMLTag.hxDelete(value: String) {
    attributes += "hx-delete" to value
}

fun HTMLTag.hxSwap(value: String) {
    attributes += "hx-swap" to value
}

fun HTMLTag.hxTarget(value: String) {
    attributes += "hx-target" to value
}

fun HTMLTag.hxTrigger(value: String) {
    attributes += "hx-trigger" to value
}

fun HTMLTag.hxIndicator(value: String) {
    attributes += "hx-indicator" to value
}

fun HTMLTag.hxSync(value: String) {
    attributes += "hx-sync" to value
}

fun HTMLTag.hxConfirm(value: String) {
    attributes += "hx-confirm" to value
}

fun HTMLTag.hxBoost(value: String) {
    attributes += "hx-boost" to value
}

fun HTMLTag.hxPushURL(value: String) {
    attributes += "hx-push-url" to value
}

fun HTMLTag.hxEncoding(value: String) {
    attributes += "hx-encoding" to value
}

fun HTMLTag.hxInclude(value: String) {
    attributes += "hx-include" to value
}

fun HTMLTag.hxExt(value: String) {
    attributes += "hx-ext" to value
}

fun HTMLTag.hxReplaceUrl(value: String) {
    attributes += "hx-ext" to value
}