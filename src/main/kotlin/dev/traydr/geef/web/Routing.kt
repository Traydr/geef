package dev.traydr.geef.web

import dev.traydr.geef.domain.Post
import dev.traydr.geef.domain.User
import dev.traydr.geef.domain.exceptions.UnsupportedFileExtensionException
import dev.traydr.geef.domain.repository.FileRepository
import dev.traydr.geef.domain.service.PostService
import dev.traydr.geef.domain.service.TokenService
import dev.traydr.geef.domain.service.UserService
import dev.traydr.geef.utils.acceptedUploadExtension
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
    val postService by inject<PostService>()

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
        get("/signup") {
            call.respondHtml(HttpStatusCode.OK) {
                signupPage()
            }
        }
        get("/login") {
            call.respondHtml(HttpStatusCode.OK) {
                loginPage()
            }
        }
        authenticate("auth-session") {
            get("/discovery") {
                call.respondHtml(HttpStatusCode.OK) {
                    discoverPage(userService.getAllUsers())
                }
            }
            get("/profile") {
                val userId = call.sessions.get<UserSession>()?.id
                if (userId != null) {
                    call.respondHtml(HttpStatusCode.OK) {
                        profilePage(userId, isOwnProfile = true)
                    }
                } else {
                    call.respondHtml(HttpStatusCode.NotFound) {
                        errorPage("User not found", 404)
                    }
                }
            }
            get("/profile/{uuid}") {
                val uuid = call.parameters["uuid"]!!
                val called: User? = userService.getUserByUUID(uuid)
                if (called != null) {
                    call.respondHtml(HttpStatusCode.OK) {
                        profilePage(called.id!!)
                    }
                } else {
                    call.respondHtml(HttpStatusCode.NotFound) {
                        errorPage("User not found", 404)
                    }
                }
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
                val details = call.receiveParameters()
                val user = User(
                    publicUUID = "",
                    email = details["email"]!!,
                    username = details["username"]!!,
                    password = details["password"]!!
                )
                userService.create(user)
                call.respondRedirect("/login")
            }
            authenticate("auth-form") {
                post("auth/login") {
                    val email = call.principal<UserIdPrincipal>()?.name.toString()
                    val user = userService.getUserbyEmail(email)
                    var token = tokenService.getTokenForEmail(email)
                    tokenService.refreshToken(token)
                    token = tokenService.getTokenForEmail(email)

                    call.sessions.set(UserSession(id = user?.id!!, value = token.value!!))
                    call.respondRedirect("/discovery")
                }
            }
            post("auth/logout") {
                call.sessions.clear<UserSession>()
                call.respondRedirect("/")
            }
            authenticate("auth-session") {
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

                                    val fileUUID = UUID.randomUUID().toString()
                                    fileName = "$fileUUID.$fileExtension"
                                    val etag = fileRepository.uploadFile(
                                        fileBytes,
                                        fileName,
                                        fileExtension!!
                                    )
                                    val created = Post(
                                        publicUUID = fileUUID,
                                        extension = fileExtension,
                                        etag = etag,
                                        author = userId,
                                        title = fileDescription,
                                        body = ""
                                    )
                                    postService.createPost(created)
                                }

                                else -> {}
                            }
                            partData.dispose()
                        }
                    } catch (e: Exception) {
                        fileRepository.deleteFile(fileName)
                        if (e is UnsupportedFileExtensionException) {
                            call.respond(HttpStatusCode.NotAcceptable, e.message.toString())
                        }
                        call.respond(HttpStatusCode.InternalServerError, "Error")
                    }
                    call.respondText("$fileName has been uploaded")
                }
                get("download/{name}") {
                    val filename = call.parameters["name"]!!
                    val file = fileRepository.downloadFile(filename)

                    if (file.isNotEmpty()) {
                        call.response.header(
                            "Content-Disposition",
                            "attachment; filename=\"${filename}\""
                        )
                        call.response.header("HX-Redirect", "/api/v1/download/$filename")
                        call.respondBytes() { file }
                    } else call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
