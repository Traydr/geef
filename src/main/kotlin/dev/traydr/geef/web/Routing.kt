package dev.traydr.geef.web

import dev.traydr.geef.domain.Token
import dev.traydr.geef.domain.User
import dev.traydr.geef.domain.exceptions.UnsupportedFileExtensionException
import dev.traydr.geef.domain.repository.FileRepository
import dev.traydr.geef.domain.service.TokenService
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.utils.acceptedUploadExtension
import dev.traydr.geef.utils.uploadPath
import dev.traydr.geef.web.pages.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.koin.ktor.ext.inject
import java.io.File
import java.util.*

fun Application.configureRouting() {
    val tokenService by inject<TokenService>()
    val userService by inject<UserService>()
    val fileRepository by inject<FileRepository>()

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
            post("auth/signup") {
                call.respond(HttpStatusCode.NotImplemented)
            }
            post("auth/login") {
                call.respond(HttpStatusCode.NotImplemented)
            }
            post("auth/logout") {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/")
            }
            authenticate {
                post("upload") {
                    val userId = call.sessions.get<UserSession>()?.id
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

                                    fileName = UUID.randomUUID().toString() + "." + fileExtension
                                    val etag = fileRepository.uploadFile(fileBytes, fileName, fileExtension!!)
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
}
