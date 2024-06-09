package dev.traydr.geef.web

import dev.traydr.geef.domain.GlobalPair
import dev.traydr.geef.domain.exceptions.UnsupportedFileExtensionException
import dev.traydr.geef.domain.service.GlobalPairsService
import dev.traydr.geef.domain.service.TokenService
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.utils.acceptedUploadExtension
import dev.traydr.geef.utils.uploadPath
import dev.traydr.geef.web.components.gsFormPost
import dev.traydr.geef.web.components.gsFormPut
import dev.traydr.geef.web.pages.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val tokenService by inject<TokenService>()
    val userService by inject<UserService>()
    val globalPairsService by inject<GlobalPairsService>()

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondHtml(status = status) { errorPage("404: Page Not Found", 404) }
        }
        exception<Throwable> { call, _ ->
            call.respondHtml(status = HttpStatusCode.InternalServerError) {
                errorPage(
                    "500: Internal Server Error",
                    500
                )
            }
        }
    }
    // Static Routes
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK) {
                indexPage()
            }
        }
        get("/healthcheck") {
            call.respond(HttpStatusCode.OK)
        }
        get("/robots.txt") {
            call.respondText {
                """
                User-agent: *
                Disallow: /api/
                Crawl-delay: 4
                """.trimIndent()
            }
        }
        staticFiles("/css", File("src/main/resources/css/styles.css"))
        staticFiles("/", File("src/main/resources/static/"))
    }
    // API routes
    routing {
        route("/api/v1/") {
            post("upload") {
                var fileDescription = ""
                var fileName = ""
                val multipartData = call.receiveMultipart()

                try {
                    multipartData.forEachPart { partData ->
                        when (partData) {
                            is PartData.FormItem -> {
                                fileDescription = partData.value
                            }

                            is PartData.FileItem -> {
                                val fileBytes = partData.streamProvider().readBytes()
                                val fileExtension =
                                    partData.originalFileName?.takeLastWhile { it != '.' }

                                if (!acceptedUploadExtension.contains(fileExtension)) {
                                    throw UnsupportedFileExtensionException("File extension '$fileExtension' is not supported")
                                }

//                            fileName = UUID.randomUUID().toString() + "." + fileExtension
                                fileName = partData.originalFileName ?: ("default$fileExtension")
                                val folder = File(uploadPath)
                                folder.mkdir()
                                File("$uploadPath$fileName").writeBytes(fileBytes)
                            }

                            else -> {}
                        }
                        partData.dispose()
                    }
                } catch (e: Exception) {
                    File("upload/$fileName").delete()

                    if (e is UnsupportedFileExtensionException) {
                        call.respond(HttpStatusCode.NotAcceptable, e.message.toString())
                    }
                    call.respond(HttpStatusCode.InternalServerError, "Error")
                }

                call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
            }
            get("download/{name}") {
                val filename = call.parameters["name"]!!
                val file = File("$uploadPath$filename")

                if (file.exists()) {
                    call.response.header("Content-Disposition", "attachment; filename=\"${file.name}\"")
                    call.response.header("HX-Redirect", "/api/v1/download/$filename")
                    call.respondFile(file)
                } else call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
